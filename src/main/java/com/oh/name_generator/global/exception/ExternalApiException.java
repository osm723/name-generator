package com.oh.name_generator.global.exception;

public class ExternalApiException extends BusinessException {

    public ExternalApiException(String message) {
        super(message, "EXTERNAL_API_ERROR");
    }

    public ExternalApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
