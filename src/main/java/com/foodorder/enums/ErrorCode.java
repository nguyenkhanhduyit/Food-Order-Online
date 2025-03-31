package com.foodorder.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    INVALID_FULLNAME(400,"Tên Chưa Hợp Lệ",HttpStatus.BAD_REQUEST),
    INVALID_LENGTH_FULLNAME(400,"Độ Dài Full Name Ít Nhất {min} Và Tối Đa {max} kí Tự",HttpStatus.FORBIDDEN),
    INVALID_EMAIL(400,"Email Chưa Hợp Lệ",HttpStatus.BAD_REQUEST),
    INVALID_LENGTH_EMAIL(400,"Độ Dài Email Ít Nhất {min} Và Tối Đa {max} kí Tự",HttpStatus.BAD_REQUEST),
    INVALID_LENGTH_PASSWORD(400,"Độ Dài Password Ít Nhất {min} Và Tối Đa {max} kí Tự",HttpStatus.BAD_REQUEST),
    INVALID_KEY(400,"KEY INVALID",HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_TOKEN(400,"Token Request Invalid",HttpStatus.BAD_REQUEST),
    BAD_CREDENTIALS(400,"Thông Tin Đăng Nhập Không Chính Xác",HttpStatus.BAD_REQUEST);

    int code;
    String message;
    HttpStatus statusCode;
}
