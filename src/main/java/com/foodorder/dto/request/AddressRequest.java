package com.foodorder.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressRequest {
    @NotBlank(message = "City Is Empty")
    String city;

    @NotBlank(message = "District Is Empty")
    String district;

    @NotBlank(message = "Street Address Is Empty")
    String streetAddress;

    public String addressComplete(){
        return this.getStreetAddress()
                .concat(" ")
                .concat(this.getDistrict())
                .concat(" ").concat(this.getCity());
    }
}
