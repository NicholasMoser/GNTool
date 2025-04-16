package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.gnt4.seq.StringEditor;
import com.github.nicholasmoser.utils.GUIUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StringEditorTool {
    private static final Logger LOGGER = Logger.getLogger(StringEditorTool.class.getName());

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
            FXMLLoader loader = new FXMLLoader(StringEditor.class.getResource("string_editor.fxml"));
            Scene scene = new Scene(loader.load());
            GUIUtils.initDarkMode(scene);
            StringEditor stringEditor = loader.getController();
            Stage stage = new Stage();
            GUIUtils.setIcons(stage);
            stringEditor.init(seqPath);
            stage.setScene(scene);
            stage.setTitle("String Editor: " + seqPath);
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error Reading Strings", e);
            Message.error("Error Reading Strings", e.getMessage());
        }

    }
}