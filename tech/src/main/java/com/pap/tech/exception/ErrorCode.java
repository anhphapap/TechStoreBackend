package com.pap.tech.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNKNOWN_ERROR(9999, "Có lỗi xảy ra", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_EXISTED(1001, "Tên đăng nhập này đã được sử dụng", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1002, "Email này đã được đăng ký trước đó",  HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Tên đăng nhập phải có ít nhất 3 ký tự",  HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Mật khẩu phải có ít nhất 6 ký tự",   HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1005, "Email sai định dạng",  HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1006, "Người dùng không tồn tại", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1007, "Người dùng chưa được xác thực",  HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1008, "Người dùng không có quyền truy cập",  HttpStatus.FORBIDDEN),
    PRODUCT_NOT_FOUND(1009, "Không tìm thấy sản phẩm", HttpStatus.NOT_FOUND);
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
