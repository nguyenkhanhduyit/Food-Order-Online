package com.foodorder.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.NumberFormat;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContactRequest {
    @Email(message = "Email Not Valid")
    @NotBlank(message = "Email không được rỗng")
    String email;

    @NumberFormat(style = NumberFormat.Style.NUMBER,pattern = "/(84|0[3|5|7|8|9])+([0-9]{8})\\b/g")
    String mobile;

    @NotBlank(message = "X is not valid")
    String x;

    @NotBlank(message = "Instagram is not valid")
    String instagram;
}
