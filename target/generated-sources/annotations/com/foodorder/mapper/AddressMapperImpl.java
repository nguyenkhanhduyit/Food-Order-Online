package com.foodorder.mapper;

import com.foodorder.dto.response.AddressResponse;
import com.foodorder.model.Address;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-02T18:01:20+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class AddressMapperImpl implements AddressMapper {

    @Override
    public AddressResponse toResponse(Address request) {
        if ( request == null ) {
            return null;
        }

        AddressResponse.AddressResponseBuilder addressResponse = AddressResponse.builder();

        addressResponse.id( request.getId() );
        addressResponse.numberPhoneContact( request.getNumberPhoneContact() );
        addressResponse.address( request.getAddress() );

        return addressResponse.build();
    }
}
