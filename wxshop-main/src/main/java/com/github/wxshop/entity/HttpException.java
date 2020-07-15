package com.github.wxshop.entity;

import org.springframework.http.HttpStatus;

public class HttpException extends RuntimeException {
    private int statusCode;
    private String message;

    private HttpException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public static HttpException forbidden(String message) {
        return new HttpException(HttpStatus.FORBIDDEN.value(), message);
    }

    public static HttpException notFound(String message) {
        return new HttpException(HttpStatus.NOT_FOUND.value(), message);
    }

    public static HttpException badRequest(String message) {
        return new HttpException(HttpStatus.BAD_REQUEST.value(), message);
    }

    public static Exception gone(String message) {
        return new HttpException(HttpStatus.GONE.value(),message);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
