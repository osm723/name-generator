package com.oh.name_generator.global.exception;

public class ValidationException extends BusinessException {

    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }
}
