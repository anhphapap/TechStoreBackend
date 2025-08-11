package com.pap.tech.exception;

public enum ErrorCode {
    UNKNOWN_ERROR(9999, "Có lỗi xảy ra"),
    USERNAME_EXISTED(1001, "Tên đăng nhập này đã được sử dụng"),
    EMAIL_EXISTED(1002, "Email này đã được đăng ký trước đó"),
    USERNAME_INVALID(1003, "Tên đăng nhập phải có ít nhất 3 ký tự"),
    PASSWORD_INVALID(1004, "Mật khẩu phải có ít nhất 6 ký tự"),
    EMAIL_INVALID(1004, "Email sai định dạng"),
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
