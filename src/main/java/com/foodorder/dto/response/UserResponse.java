package com.foodorder.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Set;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
        Long id;
        String avatarUrl;
        String fullName;
        String email;
        Set<String> roles;
}
