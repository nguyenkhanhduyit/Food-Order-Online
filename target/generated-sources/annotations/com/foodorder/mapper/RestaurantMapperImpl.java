package com.foodorder.mapper;

import com.foodorder.dto.request.AddressRequest;
import com.foodorder.dto.request.ContactRequest;
import com.foodorder.dto.request.RestaurantRequest;
import com.foodorder.dto.response.AddressResponse;
import com.foodorder.dto.response.ContactResponse;
import com.foodorder.dto.response.RestaurantResponse;
import com.foodorder.dto.response.UserResponse;
import com.foodorder.model.Address;
import com.foodorder.model.Contact;
import com.foodorder.model.Restaurant;
import com.foodorder.model.User;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-02T18:01:21+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class RestaurantMapperImpl implements RestaurantMapper {

    @Override
    public RestaurantResponse toResponse(Restaurant request) {
        if ( request == null ) {
            return null;
        }

        RestaurantResponse restaurantResponse = new RestaurantResponse();

        restaurantResponse.setId( request.getId() );
        restaurantResponse.setUser( userToUserResponse( request.getUser() ) );
        restaurantResponse.setName( request.getName() );
        restaurantResponse.setDescription( request.getDescription() );
        restaurantResponse.setCuisineType( request.getCuisineType() );
        restaurantResponse.setAddress( addressToAddressResponse( request.getAddress() ) );
        restaurantResponse.setContact( contactToContactResponse( request.getContact() ) );
        restaurantResponse.setOpenTime( request.getOpenTime() );
        restaurantResponse.setLogoUrl( request.getLogoUrl() );
        List<String> list = request.getGalleryUrls();
        if ( list != null ) {
            restaurantResponse.setGalleryUrls( new ArrayList<String>( list ) );
        }
        restaurantResponse.setRegistrationDate( request.getRegistrationDate() );

        return restaurantResponse;
    }

    @Override
    public Restaurant toRestaurant(RestaurantRequest request) {
        if ( request == null ) {
            return null;
        }

        Restaurant.RestaurantBuilder restaurant = Restaurant.builder();

        restaurant.name( request.getName() );
        restaurant.description( request.getDescription() );
        restaurant.cuisineType( request.getCuisineType() );
        restaurant.address( addressRequestToAddress( request.getAddress() ) );
        restaurant.contact( contactRequestToContact( request.getContact() ) );
        restaurant.openTime( request.getOpenTime() );

        return restaurant.build();
    }

    @Override
    public Restaurant updateRestaurant(Restaurant target, RestaurantRequest request) {
        if ( request == null ) {
            return target;
        }

        target.setName( request.getName() );
        target.setDescription( request.getDescription() );
        target.setCuisineType( request.getCuisineType() );
        target.setOpenTime( request.getOpenTime() );

        return target;
    }

    protected UserResponse userToUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.id( user.getId() );
        userResponse.avatarUrl( user.getAvatarUrl() );
        userResponse.fullName( user.getFullName() );
        userResponse.email( user.getEmail() );
        Set<String> set = user.getRoles();
        if ( set != null ) {
            userResponse.roles( new LinkedHashSet<String>( set ) );
        }

        return userResponse.build();
    }

    protected AddressResponse addressToAddressResponse(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressResponse.AddressResponseBuilder addressResponse = AddressResponse.builder();

        addressResponse.id( address.getId() );
        addressResponse.numberPhoneContact( address.getNumberPhoneContact() );
        addressResponse.address( address.getAddress() );

        return addressResponse.build();
    }

    protected ContactResponse contactToContactResponse(Contact contact) {
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

    protected Address addressRequestToAddress(AddressRequest addressRequest) {
        if ( addressRequest == null ) {
            return null;
        }

        Address.AddressBuilder address = Address.builder();

        address.numberPhoneContact( addressRequest.getNumberPhoneContact() );

        return address.build();
    }

    protected Contact contactRequestToContact(ContactRequest contactRequest) {
        if ( contactRequest == null ) {
            return null;
        }

        Contact contact = new Contact();

        contact.setEmail( contactRequest.getEmail() );
        contact.setMobile( contactRequest.getMobile() );
        contact.setX( contactRequest.getX() );
        contact.setInstagram( contactRequest.getInstagram() );

        return contact;
    }
}
