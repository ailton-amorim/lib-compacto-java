package org.compacto.parser.exceptions;

public class MalFormatException extends RuntimeException {
    public MalFormatException(String message) {
        super(message);
    }

    public MalFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalFormatException(Throwable cause) {
        super(cause);
    }
}
