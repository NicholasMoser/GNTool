package com.github.nicholasmoser.workspace;

/**
 * A single file in the workspace. The hash and modified date/time are used to track if the file
 * has been modified. There is also an optional fpk file path and if it is compressed for files
 * contained inside FRK archive files.
 */
public record WorkspaceFile(String filePath, int hash, long modifiedDtTm, String fpkFilePath,
                            boolean compressed) {

}
