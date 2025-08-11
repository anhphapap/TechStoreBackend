package com.pap.tech.exception;

public class AppException extends RuntimeException {
    public AppException(ErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }

    private ErrorCode code;

    public ErrorCode getCode() {
        return code;
    }

    public void setCode(ErrorCode code) {
        this.code = code;
    }
}
