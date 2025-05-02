package com.foodorder.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryRequest {
    @NotBlank(message = "Hãy nhập loại muốn tìm ")
    private String query;
}
