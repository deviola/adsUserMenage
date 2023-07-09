package com.ostasz.ads.exception;

public class FileExportException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "An error occurred while creating file with users.";

    public FileExportException() {
        super(DEFAULT_MESSAGE);
    }
}
