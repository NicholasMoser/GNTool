package com.github.nicholasmoser.gnt4.seq;

import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;

import java.nio.file.Path;
import java.util.List;

public class StringEditor {
    public Path seqPath;
    public int currentLength;
    public TextArea strings;

    public void save() {
        String text = strings.getText();
        List<String> lines = text.lines().toList();
        for (int i = 0; i < lines.size(); i += 2) {
            String name = lines.get(i);
            String combo = lines.get(i + 1);
        }
    }

    public void init(Path seqPath, String text, int currentLength) {
        this.seqPath = seqPath;
        this.currentLength = currentLength;
        strings.setText(text);
    }
}
