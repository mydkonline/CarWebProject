package com.motionvolt.carcare.adapter.in.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private final boolean success;
    private final String message;
    private final String session;

    private ApiResponse(boolean success, String message, String session) {
        this.success = success;
        this.message = message;
        this.session = session;
    }

    public static ApiResponse of(boolean success, String message) {
        return new ApiResponse(success, message, null);
    }

    public static ApiResponse withSession(String message, String session) {
        return new ApiResponse(true, message, session);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getSession() {
        return session;
    }
}
