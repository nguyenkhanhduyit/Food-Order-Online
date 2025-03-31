package com.foodorder.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    @NotBlank(message = "INVALID_FULLNAME")
    @Size(min = 10, max = 30, message = "INVALID_LENGTH_FULLNAME")
    String fullName;

    @Email(message = "INVALID_EMAIL")
    String email;
    // @gmail.com
    @NotBlank(message = "INVALID_PASSWORD")
    @Size(min = 7,max = 28 ,message = "INVALID_LENGTH_PASSWORD")
    String password;
}
