package org.exemple;

public class NoCoinsForChangeException extends RuntimeException {

    public NoCoinsForChangeException(String message) {
        super(message);
    }
}
