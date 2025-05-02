package com.foodorder.mapper;

import com.foodorder.dto.request.UserRequest;
import com.foodorder.dto.response.AuthResponse;
import com.foodorder.dto.response.UserResponse;
import com.foodorder.model.User;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-02T18:01:21+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserRequest request) {
        if ( request == null ) {
            return null;
        }

        User user = new User();

        user.setFullName( request.getFullName() );
        user.setEmail( request.getEmail() );

        return user;
    }

    @Override
    public UserResponse toUserResponse(User user) {
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

    @Override
    public AuthResponse toAuthResponse(User user) {
        if ( user == null ) {
            return null;
        }

        AuthResponse.AuthResponseBuilder authResponse = AuthResponse.builder();

        Set<String> set = user.getRoles();
        if ( set != null ) {
            authResponse.roles( new LinkedHashSet<String>( set ) );
        }

        return authResponse.build();
    }
}
