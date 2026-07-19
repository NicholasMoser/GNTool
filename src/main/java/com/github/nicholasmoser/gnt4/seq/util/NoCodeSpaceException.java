package com.github.nicholasmoser.gnt4.seq.util;

import java.io.IOException;

/**
 * An exception thrown if there is no space to add new code in the SEQ file.
 */
public class NoCodeSpaceException extends IOException {
    public NoCodeSpaceException() {
        super();
    }

    public NoCodeSpaceException(String message) {
        super(message);
    }

    public NoCodeSpaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoCodeSpaceException(Throwable cause) {
        super(cause);
    }
}
