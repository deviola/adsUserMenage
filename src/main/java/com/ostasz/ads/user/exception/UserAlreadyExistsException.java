package com.ostasz.ads.user.exception;

public class UserAlreadyExistsException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "User with provided PESEL already exists";

    public UserAlreadyExistsException() {
        super(DEFAULT_MESSAGE);
    }
}

