package com.centarius.gwizd.model;

public class UploadException extends Exception {
    public UploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public UploadException(String message) {
        super(message);
    }
}
