package org.exemple.exceptions;

public class CoinNotAcceptedException extends RuntimeException {

    public CoinNotAcceptedException(String message) {
        super(message);
    }
}
