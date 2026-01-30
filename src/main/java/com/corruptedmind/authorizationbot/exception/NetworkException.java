package com.corruptedmind.authorizationbot.exception;

public class NetworkException extends RuntimeException {
    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
