package com.ostasz.ads.user.exception;

public class FileExportException extends Exception {

    private static final String DEFAULT_MESSAGE = "An error occurred while creating file with users.";

    public FileExportException() {
        super(DEFAULT_MESSAGE);
    }
}
