package com.corruptedmind.authorizationbot.exception;

public class HttpRequestException extends RuntimeException {
    public int statusCode;

    public HttpRequestException(String message) {
        super(message);
    }
}
