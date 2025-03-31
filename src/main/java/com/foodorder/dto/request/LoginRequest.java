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
public class LoginRequest {
        @Email(message = "INVALID_EMAIL")
        String email;

        @NotBlank(message = "INVALID_PASSWORD")
        @Size(min = 7,max = 28 ,message = "INVALID_LENGTH_PASSWORD")
        String password;
}
