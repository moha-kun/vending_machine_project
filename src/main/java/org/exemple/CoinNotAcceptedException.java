package org.exemple;

public class CoinNotAcceptedException extends RuntimeException {

    public CoinNotAcceptedException(String message) {
        super(message);
    }
}
