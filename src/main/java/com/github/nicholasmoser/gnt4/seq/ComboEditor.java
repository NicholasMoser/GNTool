package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.gnt4.seq.util.ComboList;
import com.github.nicholasmoser.gnt4.seq.util.NoCodeSpaceException;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComboEditor {
    private static final Logger LOGGER = Logger.getLogger(ComboEditor.class.getName());
    public Path seqPath;
    public int currentLength;
    public TextArea strings;

    public void save() {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(seqPath);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to read bytes from file" + seqPath, e);
            Message.error("Failed to read bytes from file " + seqPath, "See log for more details.");
            return;
        }
        String text = strings.getText();
        // First attempt, do not overwrite code that could still be in use
        try {
            byte[] newBytes = ComboList.writeCombos(text, bytes, false);
            Files.write(seqPath, newBytes);
            // Success
            return;
        } catch (NoCodeSpaceException e) {
            boolean confirm = Message.warnConfirmation("No Code Space", e.getMessage());
            if (!confirm) {
                return;
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to write bytes to file" + seqPath, e);
            Message.error("Failed to write bytes to file " + seqPath, "See log for more details.");
            return;
        }
        // Second attempt, user has confirmed to force write
        try {
            byte[] newBytes = ComboList.writeCombos(text, bytes, true);
            Files.write(seqPath, newBytes);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to write bytes to file" + seqPath, e);
            Message.error("Failed to write bytes to file " + seqPath, "See log for more details.");
        }
    }

    public void init(Path seqPath) throws IOException {
        this.seqPath = seqPath;
        byte[] bytes = Files.readAllBytes(seqPath);
        this.currentLength = ComboList.readCombosLength(bytes);
        strings.setText(ComboList.comboBytesToString(bytes));
    }
}
