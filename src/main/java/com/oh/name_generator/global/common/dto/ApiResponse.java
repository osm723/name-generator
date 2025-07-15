package com.oh.name_generator.global.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private String code;
    private LocalDateTime timestamp;

    private ApiResponse(boolean success, T data, String message, String code) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, "성공", "SUCCESS");
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message, "SUCCESS");
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(true, null, "성공", "SUCCESS");
    }

    public static <T> ApiResponse<T> error(String message, String code) {
        return new ApiResponse<>(false, null, message, code);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, null, message, "ERROR");
    }
}
