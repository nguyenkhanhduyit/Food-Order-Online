package com.foodorder.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurant_dto")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantDTO {

    Long restaurantId;
    Long foodId;

    @Column(length = 10000) // Độ dài đủ lớn để lưu danh sách ảnh
    String imagesJson;

    @Transient// không lưu hoặc truy vấn trực tiếp ,nó chỉ tồn tại trong logic ứng dụng.
    @JsonIgnore
    public List<String> getImagesJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return imagesJson != null ? objectMapper.readValue(imagesJson, new TypeReference<List<String>>() {}) : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public void setImagesJson(List<String> images) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.imagesJson = objectMapper.writeValueAsString(images);
        } catch (IOException e) {
            this.imagesJson = "[]";
        }
    }
}
