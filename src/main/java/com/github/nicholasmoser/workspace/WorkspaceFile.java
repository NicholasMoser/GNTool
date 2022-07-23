package com.github.nicholasmoser.workspace;

public record WorkspaceFile(String filePath, int hash, long modifiedDtTm, String fpkFilePath,
                            boolean compressed) {

}
