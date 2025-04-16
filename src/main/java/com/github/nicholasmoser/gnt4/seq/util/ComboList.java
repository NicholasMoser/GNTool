package com.github.nicholasmoser.gnt4.seq.util;

import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class ComboList {
    public static final String TABLE_TWO = "----------------------TABLE TWO----------------------";
    private static final Logger LOGGER = Logger.getLogger(ComboList.class.getName());

    // TODO: Transformation characters in vanilla have the two string tables separated by 0xFFFFFFFF
    // Naruto and Sasuke

    // TODO: Check for 0xCC after the table to see if a clean expansion can be done

    // TODO: Warn user to update any pointers in cases where the character has multiple transformations. The pointers are
    // likely referenced in seq code right before the string table.

    /**
     * Reads the list of combos at the given offset.
     *
     * @param bytes The bytes to read from.
     * @return The combos in text form newline separated, e.g. name\ncombo\n
     * @throws IOException If the combos cannot be read.
     */
    public static String readCombos(byte[] bytes) throws IOException {
        int startOffset = readStartOffset(bytes);
        StringBuilder sb = new StringBuilder();
        ByteStream bs = new ByteStream(bytes);
        bs.seek(startOffset);

        int comboLength = bs.readWord();

        // Read combo table
        int count = 0;
        while(bs.peekWord() != 0xFFFFFFFF && bs.peekWord() != 0xBBBBBBBB && bs.peekWord() != 0xCCCCCCCC) {
            readCombo(sb, bs);
            count++;
        }
        if (count != comboLength) {
            LOGGER.warning(String.format("Expected %d combos but found %d", comboLength, count));
        }

        // Check for and read second combo table (Naruto, Sasuke, Kakashi, and Sakon transformation tables)
        bs.skipWord(); // Skip 0xFFFFFFFF
        while (bs.peekWord() == 0) {
            bs.skipWord(); // Skip null padding
        }
        bs.skipWord(); // Skip combo table length
        byte[] next = bs.peekBytes(5);
        if (SeqHelper.isCombo(next)) {
            sb.append('\n');
            sb.append(TABLE_TWO);
            while(bs.peekWord() != 0xFFFFFFFF && bs.peekWord() != 0xBBBBBBBB && bs.peekWord() != 0xCCCCCCCC) {
                readCombo(sb, bs);
            }
        }

        return sb.toString();
    }

    /**
     * Reads the length of the combo table.
     *
     * @param bytes The bytes to read from.
     * @return The length of the combo table.
     * @throws IOException If the combo table length cannot be read.
     */
    public static int readCombosLength(byte[] bytes) throws IOException {
        int startOffset = readStartOffset(bytes);
        int endOffset = -1;
        ByteStream bs = new ByteStream(bytes);
        bs.seek(startOffset);
        bs.skipWord(); // skip string table length
        while (bs.bytesAreLeft()) {
            int word = bs.readWord();
            if (word == -1 || word == 0xBBBBBBBB || word == 0xCCCCCCCC) {
                endOffset = bs.offset();
                break;
            }
        }
        if (endOffset == -1) {
            throw new IOException("Unable to find the end of the combo list");
        }

        // Check for and read second combo table (Naruto, Sasuke, Kakashi, and Sakon transformation tables)
        while (bs.peekWord() == 0) {
            bs.skipWord(); // Skip null padding
        }
        bs.skipWord(); // Skip combo table length
        byte[] next = bs.peekBytes(5);
        if (SeqHelper.isCombo(next)) {
            // Two combo tables
            while (bs.bytesAreLeft()) {
                int word = bs.readWord();
                if (word == -1 || word == 0xBBBBBBBB || word == 0xCCCCCCCC) {
                    endOffset = bs.offset();
                    return endOffset - startOffset;
                }
            }
        } else {
            // One combo table
            return endOffset - startOffset;
        }
        throw new IOException("Unable to find the end of the combo list");
    }

    /**
     * Get the start offset of the combo list.
     *
     * @param bytes The seq bytes.
     * @return the start offset of the combo list.
     * @throws IOException If the combo list cannot be found.
     */
    public static int readStartOffset(byte[] bytes) throws IOException {
        // Subtract 4 from offset to include the combo list size, the true start of the combo list
        int offset = findSequenceOffset(bytes, SeqHelper.COMBO_JAPANESE);
        if (offset > -1) {
            return offset - 4;
        }
        offset = findSequenceOffset(bytes, SeqHelper.REPEATED_HITS);
        if (offset > -1) {
            return offset - 4;
        }
        offset = findSequenceOffset(bytes, SeqHelper.COMBO);
        if (offset > -1) {
            return offset - 4;
        }
        offset = findSequenceOffset(bytes, SeqHelper.CHORD);
        if (offset > -1) {
            return offset - 4;
        }
        offset = findSequenceOffset(bytes, SeqHelper.ROUTINE);
        if (offset > -1) {
            return offset - 4;
        }
        offset = findSequenceOffset(bytes, SeqHelper.SEQ);
        if (offset > -1) {
            return offset - 4;
        }
        throw new IOException("Unable to find start offset of combo list");
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
        if (!sb.isEmpty()) {
            sb.append("\n");
        }
        sb.append(comboName);

        // Get combo
        String combo = readAlignedCString(bs);
        sb.append('\n');
        sb.append(combo);
    }

    /**
     * Read a shift-jis encoded CString that is four-byte aligned with null bytes after it.
     *
     * @param bs The byte stream to read the bytes from.
     * @return The CString.
     * @throws IOException If the string could not be read.
     */
    private static String readAlignedCString(ByteStream bs) throws IOException {
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
