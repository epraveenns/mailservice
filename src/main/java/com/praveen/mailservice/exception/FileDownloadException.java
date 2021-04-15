package com.praveen.mailservice.exception;

public class FileDownloadException extends RuntimeException {
    public FileDownloadException(String message) {
        super(message);
    }

    public FileDownloadException(String message, Exception e) {
        super(message, e);
    }
}
