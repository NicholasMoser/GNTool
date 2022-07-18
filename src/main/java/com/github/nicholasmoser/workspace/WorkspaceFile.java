package com.github.nicholasmoser.workspace;

public record WorkspaceFile(String filePath, int hash, int modifiedDtTm, String fpkFilePath) {

}
