package org.compacto.parser.exceptions;

public class CompactoException extends Exception {
    public CompactoException(String message) {
        super(message);
    }

    public CompactoException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompactoException(Throwable cause) {
        super(cause);
    }
}
