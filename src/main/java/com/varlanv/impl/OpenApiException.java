package com.varlanv.impl;

public class OpenApiException extends RuntimeException {

    public OpenApiException(String message) {
        super(message);
    }

    public OpenApiException(String message, Object... format) {
        super(message.formatted(format));
    }
}
