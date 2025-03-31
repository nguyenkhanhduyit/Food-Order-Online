package com.foodorder.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantRequest {
    @NotBlank(message = "Thiếu tên nhà hàng")
    String name;

    @NotBlank(message = "Thiếu mô tả nhà hàng")
    String description;

    @NotBlank(message = "Thiếu loại ẩm thực của nhà hàng ")
    String cuisineType;

    @NotNull(message = "Thiếu địa chỉ nhà hàng")
    AddressRequest address;

    @NotNull(message = "Thiếu thông tin liên hệ của nhà hàng")
    ContactRequest contact;

    @NotBlank(message = "Thiếu thời gian hoạt động của nhà hàng")
    String openTime;

    @NotNull(message = "Thiếu ảnh mô tả nhà hàng")
    List<String> images;
}
