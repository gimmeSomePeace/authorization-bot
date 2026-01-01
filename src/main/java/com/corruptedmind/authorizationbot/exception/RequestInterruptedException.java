package com.corruptedmind.authorizationbot.exception;

public class RequestInterruptedException extends RuntimeException {
    public RequestInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}
