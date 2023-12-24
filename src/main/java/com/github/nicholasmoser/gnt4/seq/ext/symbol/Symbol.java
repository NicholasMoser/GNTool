package com.github.nicholasmoser.gnt4.seq.ext.symbol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public interface Symbol {

    /**
     * @return The name of this symbol.
     */
    String name();

    /**
     * @return The offset to the actual (non-header) data for this symbol.
     */
    int dataOffset();

    /**
     * @return The full length of this symbol in bytes.
     */
    int length();

    /**
     * @return The full bytes of this symbol.
     */
    byte[] bytes();

    /**
     * Returns the 16-byte aligned, UTF-8 encoded bytes of the name of this seq edit.
     * It is terminated with null bytes.
     *
     * @param name The name to get the bytes of.
     * @return the 16-byte aligned, UTF-8 encoded bytes of the name of this seq edit.
     */
    static byte[] getNameBytes(String name) {
        try {
            byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(nameBytes);
            int nullBytes = 16 - (nameBytes.length % 16); // 16-byte aligned
            baos.write(new byte[nullBytes]);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
