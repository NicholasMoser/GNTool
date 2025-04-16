package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.gnt4.seq.util.ComboList;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.nio.file.Files;
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

    public void init(Path seqPath) throws IOException {
        this.seqPath = seqPath;
        byte[] bytes = Files.readAllBytes(seqPath);
        this.currentLength = ComboList.readCombosLength(bytes);
        strings.setText(ComboList.readCombos(bytes));
    }
}
