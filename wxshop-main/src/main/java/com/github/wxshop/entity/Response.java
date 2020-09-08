package com.github.wxshop.entity;

public class Response<T> { // 这里建议写成泛型的,因为后面还有不同的响应值
    private T data;
    private String message;

    public static <T> Response<T> of(T data) {
        return new Response<T>(null, data);
    }

    public static <T> Response<T> of(String message, T data) {
        return new Response<T>(null, data);
    }

    public Response() {
    }

    public Response(String message, T data) {
        this.data = data;
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
