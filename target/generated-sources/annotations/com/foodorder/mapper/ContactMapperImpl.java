package com.foodorder.mapper;

import com.foodorder.dto.request.ContactRequest;
import com.foodorder.dto.response.ContactResponse;
import com.foodorder.model.Contact;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-01T00:00:40+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class ContactMapperImpl implements ContactMapper {

    @Override
    public Contact toContact(ContactRequest request) {
        if ( request == null ) {
            return null;
        }

        Contact contact = new Contact();

        contact.setEmail( request.getEmail() );
        contact.setMobile( request.getMobile() );
        contact.setX( request.getX() );
        contact.setInstagram( request.getInstagram() );

        return contact;
    }

    @Override
    public ContactResponse toResponse(Contact contact) {
        if ( contact == null ) {
            return null;
        }

        ContactResponse.ContactResponseBuilder contactResponse = ContactResponse.builder();

        contactResponse.email( contact.getEmail() );
        contactResponse.mobile( contact.getMobile() );
        contactResponse.x( contact.getX() );
        contactResponse.instagram( contact.getInstagram() );

        return contactResponse.build();
    }
}
