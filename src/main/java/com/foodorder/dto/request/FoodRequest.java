package com.foodorder.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class FoodRequest {
    @NotBlank(message = "Hãy cung cấp tên sản phẩm để tạo")
    private String name;
    @NotBlank(message = "Hãy cung cấp mô tả sản phẩm để tạo")
    private String description;
    @NotNull(message = "Hãy cung cấp giá sản phẩm để tạo")
    @Positive(message = "Giá sản phẩm phải > 0")
    private BigDecimal price;
    @NotNull(message = "Ảnh sản phẩm phải có")
    private List<String> images;
    @NotNull(message = "Thiếu loại sản phẩm")
    private Long foodCategoryId;
    @NotNull(message = "Thiếu nhà hàng cho sản phẩm")
    private Long restaurantId;
    @NotNull(message = "Thiếu trạng thái sản phẩm")
    private boolean available;
    @NotNull(message = "Thiếu phân loại sản phẩm có phải thực phẩm chay không?")
    private boolean isVegetarian;
    @NotNull(message = "Thiếu phân loại sản phẩm có phải thực phẩm biển không?")
    private boolean isSeasonal;
    @NotNull(message = "Thiếu thành phần cho thực phẩm")
    private List<Long> ingredientItemsId;
}
