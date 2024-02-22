package org.exemple.exceptions;

public class NoCoinsForChangeException extends RuntimeException {

    public NoCoinsForChangeException(String message) {
        super(message);
    }
}
