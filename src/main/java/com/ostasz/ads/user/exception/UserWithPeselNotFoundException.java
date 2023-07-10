package com.ostasz.ads.user.exception;

public class UserWithPeselNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "User with provided PESEL not found";

    public UserWithPeselNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}