package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.gnt4.seq.StringEditor;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import com.github.nicholasmoser.utils.GUIUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StringEditorTool {

    private static final Logger LOGGER = Logger.getLogger(StringEditorTool.class.getName());
    private static final String TABLE_TWO = "----------------------TABLE TWO----------------------";

    private static File currentDirectory = GNTool.USER_HOME;

    /**
     * Query the user to open a seq file with the StringEditorTool. Defaults to the user home but will
     * remember the last directory selected from for subsequent calls.
     *
     * @throws IOException If any I/O exception occurs.
     */
    public static void open() throws IOException {
        Optional<Path> optionalSeq = Choosers.getInputSeq(currentDirectory);
        if (optionalSeq.isEmpty()) {
            return;
        }
        Path seqPath = optionalSeq.get();
        currentDirectory = seqPath.getParent().toFile();
        open(seqPath);
    }

    /**
     * Opens a seq file with the StringEditorTool.
     *
     * @param seqPath The seq to open.
     * @throws IOException If any I/O exception occurs.
     */
    public static void open(Path seqPath) throws IOException {

        try {
            byte[] bytes = Files.readAllBytes(seqPath);
            int offset = firstStartOfCombos(bytes);
            int length = getCombosLength(bytes, offset);
            String strings = readCombos(bytes, offset);
            System.out.println(strings);
            FXMLLoader loader = new FXMLLoader(StringEditor.class.getResource("string_editor.fxml"));
            Scene scene = new Scene(loader.load());
            GUIUtils.initDarkMode(scene);
            StringEditor stringEditor = loader.getController();
            Stage stage = new Stage();
            GUIUtils.setIcons(stage);
            stringEditor.init(seqPath, strings, length);
            stage.setScene(scene);
            stage.setTitle("String Editor: " + seqPath);
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error Reading Strings", e);
            Message.error("Error Reading Strings", e.getMessage());
        }

    }

    /**
     * Get the length of the combo table.
     *
     * @param bytes The bytes to read from.
     * @param offset The starting point to read bytes at.
     * @return The length of the combo table.
     * @throws IOException If the combo table length cannot be read.
     */
    private static int getCombosLength(byte[] bytes, int offset) throws IOException {
        for (int i = offset; i < bytes.length; i += 4) {
            if (bytes[i] == (byte) 0xFF &&
                bytes[i + 1] == (byte) 0xFF &&
                bytes[i + 2] == (byte) 0xFF &&
                bytes[i + 3] == (byte) 0xFF) {
                return i - offset;
            }
        }
        throw new IOException("Unable to find the end of the combo list");
    }

    // TODO: Transformation characters in vanilla have the two string tables separated by 0xFFFFFFFF
    // Naruto and Sasuke

    // TODO: Check for 0xCC after the table to see if a clean expansion can be done

    // TODO: Warn user to update any pointers in cases where the character has multiple transformations. The pointers are
    // likely referenced in seq code right before the string table.

    /**
     * Read the list of combos at the given offset.
     *
     * @param bytes The bytes to read from.
     * @param offset The offset to start reading combo bytes at.
     * @return The combos in text form newline separated, e.g. name\ncombo\n
     * @throws IOException If the combos cannot be read.
     */
    private static String readCombos(byte[] bytes, int offset) throws IOException {
        StringBuilder sb = new StringBuilder();
        ByteStream bs = new ByteStream(bytes);
        bs.seek(offset);

        // Read combo table
        while(bs.peekWord() != 0xFFFFFFFF && bs.peekWord() != 0xBBBBBBBB && bs.peekWord() != 0xCCCCCCCC) {
            readCombo(sb, bs);
        }

        // Check for and read second combo table, e.g. Naruto and Sasuke transformation tables
        bs.skipWord(); // Skip 0xFFFFFFFF
        while (bs.peekWord() == 0) {
            bs.skipWord(); // Skip null padding
        }
        bs.skipWord(); // Skip combo table length
        byte[] next = bs.peekBytes(5);
        if (SeqHelper.isCombo(next)) {
            sb.append(TABLE_TWO + "\n");
            while(bs.peekWord() != 0xFFFFFFFF && bs.peekWord() != 0xBBBBBBBB && bs.peekWord() != 0xCCCCCCCC) {
                readCombo(sb, bs);
            }
        }

        return sb.toString();
    }

    /**
     * Read a single combo into the string builder.
     *
     * @param sb The string builder to insert the combo text into.
     * @param bs The byte stream to read the combo from.
     * @throws IOException If the combo cannot be read.
     */
    private static void readCombo(StringBuilder sb, ByteStream bs) throws IOException {
        // Get combo name
        String comboName = readAlignedCString(bs);
        sb.append(comboName);
        sb.append("\n");

        // Get combo
        String combo = readAlignedCString(bs);
        sb.append(combo);
        sb.append('\n');
    }

    /**
     * Read a shift-jis encoded CString that is four-byte aligned with null bytes after it.
     *
     * @param bs The byte stream to read the bytes from.
     * @return The CString.
     * @throws IOException If the string could not be read.
     */
    public static String readAlignedCString(ByteStream bs) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int curr = bs.read();
        while (curr != 0  && curr != 0xFF) {
            if (curr == -1) {
                throw new IOException("Unexpected end of file when reading combo");
            }
            baos.write(curr);
            curr = bs.read();
        }
        ByteUtils.align(bs, 4);
        return baos.toString("shift-jis");
    }

    /**
     * Find the start of the combo table.
     *
     * @param bytes The seq bytes to search.
     * @return The start of the combo table.
     * @throws IOException If the combo table is not found.
     */
    public static int firstStartOfCombos(byte[] bytes) throws IOException {
        int offset = findSequenceOffset(bytes, SeqHelper.COMBO_JAPANESE);
        if (offset > -1) {
            return offset;
        }
        offset = findSequenceOffset(bytes, SeqHelper.REPEATED_HITS);
        if (offset > -1) {
            return offset;
        }
        offset = findSequenceOffset(bytes, SeqHelper.COMBO);
        if (offset > -1) {
            return offset;
        }
        offset = findSequenceOffset(bytes, SeqHelper.CHORD);
        if (offset > -1) {
            return offset;
        }
        offset = findSequenceOffset(bytes, SeqHelper.ROUTINE);
        if (offset > -1) {
            return offset;
        }
        offset = findSequenceOffset(bytes, SeqHelper.SEQ);
        if (offset > -1) {
            return offset;
        }
        throw new IOException("Unable to find start of combo list");
    }

    /**
     * Find the first offset of a sequence in data.
     *
     * @param data The data to search.
     * @param sequence The sequence to search for.
     * @return The first offset or -1 if not found.
     */
    private static int findSequenceOffset(byte[] data, byte[] sequence) {
        if (sequence.length == 0 || data.length < sequence.length) {
            return -1;
        }
        outer:
        for (int i = 0; i <= data.length - sequence.length; i++) {
            for (int j = 0; j < sequence.length; j++) {
                if (data[i + j] != sequence[j]) {
                    continue outer;
                }
            }
            return i; // Match found at offset i
        }

        return -1; // No match
    }
}