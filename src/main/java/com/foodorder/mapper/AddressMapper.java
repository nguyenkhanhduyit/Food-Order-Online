package com.foodorder.mapper;

import com.foodorder.dto.request.AddressRequest;
import com.foodorder.dto.response.AddressResponse;
import com.foodorder.model.Address;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface AddressMapper {

    public AddressResponse toResponse(Address request);
}
