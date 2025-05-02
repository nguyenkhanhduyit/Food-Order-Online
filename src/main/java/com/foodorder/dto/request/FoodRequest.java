package com.foodorder.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class FoodRequest {

    @NotBlank(message = "Hãy cung cấp tên sản phẩm để tạo")
    private String name;

    private String description;

    @NotNull(message = "Hãy cung cấp giá sản phẩm để tạo")
    @Positive(message = "Giá sản phẩm phải > 0")
    private BigDecimal price;

    private MultipartFile imageLogo;

    private List<MultipartFile> imagesGallery;

    @NotNull(message = "Hãy cung cấp loại thực phẩm cho thực phẩm")
    private Long foodCategoryId;

    @NotNull(message = "Hãy cung cấp nhà hàng cho thực phẩm")
    private Long restaurantId;

    private boolean available;

    private boolean isVegetarian;

    private boolean isSeasonal;

    private List<Long> ingredientCategoryId;

    private List<Long> ingredientItemsId;

    public void setName(String name) {
        this.name = name.trim().replace("  "," ");;
    }

    public void setDescription(String description) {
        this.description = description.trim().replace("  "," ");;
    }
}
