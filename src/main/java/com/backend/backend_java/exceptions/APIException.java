package com.backend.backend_java.exceptions;

public class APIException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String errorCode;

    public APIException() {
        super();
    }

    public APIException(String message) {
        super(message);
    }

    public APIException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
