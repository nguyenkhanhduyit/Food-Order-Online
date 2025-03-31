package com.foodorder.mapper;

import com.foodorder.dto.request.ContactRequest;
import com.foodorder.dto.response.ContactResponse;
import com.foodorder.model.Contact;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactMapper {

     public Contact toContact(ContactRequest request);
     public ContactResponse toResponse(Contact contact);
}
