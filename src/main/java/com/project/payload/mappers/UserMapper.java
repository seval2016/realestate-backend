package com.project.payload.mappers;

import com.project.entity.concretes.user.User;
import com.project.entity.concretes.user.UserRole;
import com.project.entity.enums.RoleType;
import com.project.payload.request.abstracts.BaseUserRequest;
import com.project.payload.request.user.UserRequest;
import com.project.payload.request.user.UserRequestWithoutPassword;
import com.project.payload.response.abstracts.BaseUserResponse;
import com.project.payload.response.user.CustomerResponse;
import com.project.payload.response.user.UserResponse;

import com.project.payload.response.user.UserResponseForTourRequest;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserResponse mapUserToUserResponse(User user) {
        // Kullanıcı nesnesinden UserResponse nesnesi oluşturuluyor
        return UserResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .userRole(user.getUserRole().stream()
                        .map(role -> role.getRole().name())
                        .collect(Collectors.toList()))  // Eğer roller null ise, boş bir liste dönüyor
                .build();  // UserResponse nesnesi oluşturuluyor ve döndürülüyor
    }

    public User mapUserRequestToUser(BaseUserRequest userRequest) {

        return User.builder()
                .username(userRequest.getUsername())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .passwordHash(userRequest.getPasswordHash())
                .phone(userRequest.getPhone())
                .email(userRequest.getEmail())
                .builtIn(userRequest.getBuiltIn())
                .build();
    }

    public CustomerResponse mapUserToCustomerResponse(User customer) {
        return  CustomerResponse.builder()
                .userId(customer.getId())
                .username(customer.getUsername())
                .firstname(customer.getFirstName())
                .lastname(customer.getLastName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .build();
    }

    public User mapUserRequestToUpdatedUser(UserRequest userRequest, Long userId) {

        return User.builder()
                .id(userId)
                .username(userRequest.getUsername())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .passwordHash(userRequest.getPasswordHash())
                .phone(userRequest.getPhone())
                .email(userRequest.getEmail())
                .build();

    }

    // Other mapping methods

    public void mapUserRequestWithoutPasswordToUser(UserRequestWithoutPassword userRequestWithoutPassword, User user) {
        user.setFirstName(userRequestWithoutPassword.getFirstName());
        user.setLastName(userRequestWithoutPassword.getLastName());
        user.setPhone(userRequestWithoutPassword.getPhone());
        user.setEmail(userRequestWithoutPassword.getEmail());
    }

    public User mapUserResponseToUser(BaseUserResponse authenticatedUser) {
        return User.builder()
                .id(authenticatedUser.getUserId())
                .username(authenticatedUser.getUsername())
                .firstName(authenticatedUser.getFirstname())
                .lastName(authenticatedUser.getLastname())
                .email(authenticatedUser.getEmail())
                .phone(authenticatedUser.getPhone())
                .userRole(authenticatedUser.getUserRole()
                        .stream()
                        .map(roleName -> {
                            UserRole userRole = new UserRole();
                            userRole.setRoleName(roleName);
                            userRole.setRole(RoleType.valueOf(roleName.toUpperCase()));
                            return userRole;
                        })
                        .collect(Collectors.toList()))
                .build();
    }


    //---------------------------------

    public UserResponseForTourRequest mapUserToUserResponseForTourRequest(User user) {
        return UserResponseForTourRequest.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }
}