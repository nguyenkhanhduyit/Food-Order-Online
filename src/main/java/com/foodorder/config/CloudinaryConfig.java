package com.foodorder.config;

import com.cloudinary.Cloudinary;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CloudinaryConfig {
//    @Value("${cloudinary.cloud-name}")
     String cloudName = "ddc9nrzyh";
//    @Value("${cloudinary.api-key}")
     String apiKey="768559914653885";
//    @Value("${cloudinary.api-secret}")
     String apiSecret="eXiZUiTAxGGf0jKTup0wYuQ1G48";

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        return new Cloudinary(config);
    }
}
