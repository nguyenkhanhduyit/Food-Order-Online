package com.foodorder.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {

    @Size(min = 10, max = 50, message = "INVALID_LENGTH_FULLNAME")
    String fullName;

    @Size(min = 7,max = 28 ,message = "INVALID_LENGTH_PASSWORD")
    String password;

    MultipartFile avatar;
}
