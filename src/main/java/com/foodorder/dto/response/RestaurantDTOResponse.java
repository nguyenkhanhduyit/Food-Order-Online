package com.foodorder.dto.response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantDTOResponse {
        String foodName;

        String imagesJson;

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
