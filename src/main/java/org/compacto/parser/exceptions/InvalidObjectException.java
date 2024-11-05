package org.compacto.parser.exceptions;

public class InvalidObjectException extends RuntimeException {
    public InvalidObjectException(String message) {
        super(message);
    }

    public InvalidObjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidObjectException(Throwable cause) {
        super(cause);
    }
}
