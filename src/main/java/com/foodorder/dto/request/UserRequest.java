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
    @Size(min = 10, max = 50, message = "INVALID_LENGTH_FULLNAME")
    String fullName;

    @Email(message = "INVALID_EMAIL")
    String email;
    // @gmail.com
    @NotBlank(message = "INVALID_PASSWORD")
    @Size(min = 7,max = 28 ,message = "INVALID_LENGTH_PASSWORD")
    String password;

    public void setFullName(String fullName) {
        this.fullName = fullName.trim().replace("  "," ");;
    }

    public void setPassword(String password) {
        this.password = password.trim().replace("  "," ");;
    }

    public void setEmail(String email) {
        this.email = email.trim().replace("  "," ");;
    }
}
