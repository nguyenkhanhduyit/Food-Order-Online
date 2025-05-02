package com.foodorder.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressRequest {

    @NotBlank(message = "Missed number phone contact")
    String numberPhoneContact;

    @NotBlank(message = "City Is Empty")
    String city;
    public void setCity(String city) {
        this.city = city.trim().replace("  "," ");
    }
    @NotBlank(message = "District Is Empty")
    String district;
    public void setDistrict(String district) {
        this.district = district.trim().replace("  "," ");
    }
    @NotBlank(message = "Street Address Is Empty")
    String streetAddress;
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress.trim().replace("  "," ");
    }
}
