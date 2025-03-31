package com.foodorder.exception;

import com.foodorder.dto.response.ApiResponse;
import com.foodorder.enums.ErrorCode;
import com.foodorder.exception.declare.ArgumentRequestInValid;
import com.foodorder.exception.declare.ResourceAlreadyExistException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
@Slf4j
public class GlobalHandlerException {

    static String MIN_ATTRIBUTE = "min";
    static String MAX_ATTRIBUTE = "max";

    @ExceptionHandler({
            ResourceAlreadyExistException.class,
            IllegalArgumentException.class,
            MalformedJwtException.class,
            RuntimeException.class,
            ArgumentRequestInValid.class,
            ClassCastException.class,
            StackOverflowError.class
            })
    public ResponseEntity<ApiResponse<String>> handleAuthException(Exception e){
            return ResponseEntity.badRequest().body(
                    ApiResponse.<String>builder()
                            .code(400)
                            .message(e.getMessage())
                            .build()
            );
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleException(MethodArgumentNotValidException e){
        String enumKey = e.getFieldError().getDefaultMessage();//ENUM KEY
        ErrorCode errorCode = ErrorCode.INVALID_KEY;// lấy enum ban đầu là Invalid Key
        Map<String, Object> attribute = null;
        try{
            errorCode = ErrorCode.valueOf(enumKey);
            ConstraintViolation constraintViolation = e.getBindingResult()
                                                        .getAllErrors()
                                                        .getFirst()
                                                        .unwrap(ConstraintViolation.class);
            attribute = constraintViolation.getConstraintDescriptor().getAttributes();
        } catch (IllegalArgumentException e2) {
            throw new IllegalArgumentException(e2);
        }
        return ResponseEntity.badRequest().body(
                ApiResponse.<String>builder()
                        .code(errorCode.getCode())
                        .message(Objects.nonNull(attribute)?
                                mapAttribute(errorCode.getMessage(),attribute): errorCode.getMessage())
                        .build()
        );
    }
    private String mapAttribute(String message, Map<String,Object> attributes){
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        String maxValue = String.valueOf(attributes.getOrDefault(MAX_ATTRIBUTE, ""));
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue)
                      .replace("{" + MAX_ATTRIBUTE + "}", maxValue);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ApiResponse<String>> handleBadCredentials(BadCredentialsException e){
        return ResponseEntity.badRequest().body(
                ApiResponse.<String>builder()
                        .code(ErrorCode.BAD_CREDENTIALS.getCode())
                        .message(ErrorCode.BAD_CREDENTIALS.getMessage()).build());
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        Throwable rootCause = e.getRootCause();
        String message = "Dữ liệu không hợp lệ.";

        if (rootCause instanceof SQLException sqlEx) {
            if (sqlEx.getErrorCode() == 1062) { // Lỗi Duplicate Entry
                message = getDuplicateFieldMessage(sqlEx.getMessage());
            } else {
                message = "Lỗi cơ sở dữ liệu: " + sqlEx.getMessage();
            }
        }

        return ResponseEntity.status(HttpStatus.CONFLICT) // 409 Conflict
                .body(ApiResponse.<String>builder()
                        .code(409)
                        .message(message)
                        .build());
    }
    private static final Pattern DUPLICATE_ENTRY_PATTERN =
            Pattern.compile("Duplicate entry '([^']*)' for key");

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+?\\d{7,15}$");  // Số điện thoại có từ 7 đến 15 số

    private String getDuplicateFieldMessage(String sqlMessage) {
        Matcher matcher = DUPLICATE_ENTRY_PATTERN.matcher(sqlMessage);
        if (matcher.find()) {
            String target = matcher.group(1); // Lấy nội dung bên trong dấu '
            System.err.println(target); // Debug xem giá trị target

            if (EMAIL_PATTERN.matcher(target).matches())
                return "Email đã được sử dụng. Vui lòng chọn email khác.";
            else if (target.contains("x.com/"))
                return "Tài khoản X đã tồn tại. Vui lòng chọn tài khoản khác.";
            else if (target.contains("instagram.com/"))
                return "Tài khoản Instagram đã tồn tại. Vui lòng chọn tài khoản khác.";
            else if (PHONE_PATTERN.matcher(target).matches())
                return "Số điện thoại đã được liên kết với nhà hàng khác. Vui lòng kiểm tra lại.";
        }
        return "Dữ liệu đã được sử dụng. Vui lòng kiểm tra lại.";
    }


}
