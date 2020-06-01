package ru.siberteam.exception;

public class InvalidInputArgException extends Exception {
    public InvalidInputArgException(String message) {
        super(message);
    }

    public InvalidInputArgException(String message, Throwable cause) {
        super(message, cause);
    }
}
