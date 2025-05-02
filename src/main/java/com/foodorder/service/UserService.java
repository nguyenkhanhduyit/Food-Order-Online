package com.foodorder.service;

import com.foodorder.config.JwtUtils;
import com.foodorder.dto.request.AddressRequest;
import com.foodorder.dto.request.UserUpdateRequest;
import com.foodorder.dto.response.AddressResponse;
import com.foodorder.dto.response.UserResponse;
import com.foodorder.exception.declare.ResourceNotAvailableException;
import com.foodorder.mapper.UserMapper;
import com.foodorder.model.Address;
import com.foodorder.model.User;
import com.foodorder.repository.AddressRepository;
import com.foodorder.repository.UserRepository;
import com.foodorder.service.iservice.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class UserService implements IUserService {

    UserRepository userRepository;
    JwtUtils jwtUtils;
    UserMapper userMapper;
    CloudinaryService cloudinaryService;
    PasswordEncoder passwordEncoder;
    AddressRepository addressRepository;

    @Override
    @Transactional(rollbackFor = Exception.class,isolation = Isolation.SERIALIZABLE,timeout = 20)
    public UserResponse updateUser(String token, UserUpdateRequest request) {
        User userFromToken = findUserByToken(token);
        if(!request.getFullName().isBlank())
            userFromToken.setFullName(request.getFullName());
        if(!request.getPassword().isBlank())
            userFromToken.setPassword(passwordEncoder.encode(request.getPassword()));
        if(!request.getAvatar().isEmpty() && request.getAvatar() != null){
            try {
                if(!userFromToken.getAvatarUrl().isBlank())
                    cloudinaryService.deleteImage(userFromToken.getAvatarUrl());
                userFromToken.setAvatarUrl(cloudinaryService.uploadImage(request.getAvatar()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return userMapper.toUserResponse(userRepository.save(userFromToken));
        /*
        Updated and test completed update User, upload Avatar, delete old image in Cloudinary
        */
    }


    @Override
    @Transactional(readOnly = true)
    public User findUserByToken(String token) {
        if(token == null || !jwtUtils.validateToken(token))
            throw new BadCredentialsException("Token Invalid");
        String email = jwtUtils.retrieveEmailFromToken(token);
        return userRepository.findByEmail(email)
                .orElseThrow( () -> new ResourceNotAvailableException("User Not Found By Email"));
    }


    @Override
    @Transactional(readOnly = true)
    public UserResponse findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow( () -> new ResourceNotAvailableException("User not found by email"));
        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 20)
    public AddressResponse addNewAddressToUser(String token, AddressRequest addressRequest){
        //gán value cho address
        Address newAddress = new Address();
        newAddress.setAddress(addressRequest.getStreetAddress()+addressRequest.getDistrict()+addressRequest.getCity());
        newAddress.setNumberPhoneContact(addressRequest.getNumberPhoneContact());
        //find user from db
        User user = findUserByToken(token);
        List<Address> countAddressOfUser = addressRepository.getAllOfUser(user.getId());
        if(countAddressOfUser.size()>2)
            throw new ResourceNotAvailableException("Number of address from User achieved limit");
        //assign address to user
        user.getAddress().add(newAddress);
        //assign user to address
        newAddress.setUser(user);
        addressRepository.save(newAddress);
        userRepository.save(user);
        return AddressResponse.builder()
                .id(newAddress.getId())
                .numberPhoneContact(newAddress.getNumberPhoneContact())
                .address(newAddress.getAddress())
                .build();
    }

    @Override
    public List<AddressResponse> deleteAddressFromUser(String token, Long id) {
        User user = findUserByToken(token);
        Address address = addressRepository.findById(id)
                .orElseThrow(
                        ()-> new ResourceNotAvailableException("Address not found"));
        if(!user.getAddress().contains(address))
            throw new ResourceNotAvailableException("User not is owner this address");
        user.getAddress().remove(address);
        userRepository.save(user);
        addressRepository.delete(address);
        return user.getAddress().stream().map(
                ad -> AddressResponse.builder()
                        .id(ad.getId())
                        .numberPhoneContact(ad.getNumberPhoneContact())
                        .address(ad.getAddress())
                        .build()
        ).toList();
        /*Updated and test completed all*/
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getAllAddressByUser(String token) {
        User user = findUserByToken(token);
        return user.getAddress().stream().map(
               address -> AddressResponse.builder()
                       .id(address.getId())
                       .numberPhoneContact(address.getNumberPhoneContact())
                       .address(address.getAddress())
                       .build()
        ).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findUserById(Long userId) {
       User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotAvailableException("User Not Found"));
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse convertUserToUserResponse(User user) {
        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 20)
    public AddressResponse updateAddress(String token, Long id,AddressRequest request) {
        User user = this.findUserByToken(token);
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotAvailableException("Address not found"));
        if(!user.getAddress().contains(address))
            throw new ResourceNotAvailableException("Address not belong to user");
        address.setAddress(request.getStreetAddress() + request.getDistrict() + request.getCity());
        address.setNumberPhoneContact(request.getNumberPhoneContact());
        return AddressResponse.builder()
                .id(address.getId())
                .numberPhoneContact(address.getNumberPhoneContact())
                .address(address.getAddress())
                .build();
    }

}
