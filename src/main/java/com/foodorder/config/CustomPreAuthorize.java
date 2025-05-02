package com.foodorder.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("CustomPreAuthorize")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CustomPreAuthorize {

    public boolean isUserRequestingTheirOwnData(Authentication authentication) {
        return authentication != null && authentication.getName() != null;
    }

}