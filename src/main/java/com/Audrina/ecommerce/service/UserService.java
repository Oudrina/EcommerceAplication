package com.Audrina.ecommerce.service;

import com.Audrina.ecommerce.dto.AddressDTO;
import com.Audrina.ecommerce.dto.UserRequest;
import com.Audrina.ecommerce.dto.UserResponse;
import com.Audrina.ecommerce.model.Address;
import com.Audrina.ecommerce.model.User;
import com.Audrina.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserResponse> fetchAllUser() {
        return userRepository.findAll()
                .stream()
                .map(
                        this::mapToUserResponse
                ).toList();


    }
    public Optional<UserResponse> fetchUser(Long id) {
        return userRepository.findById(id).map(
                this::mapToUserResponse
        );

    }

    public void createUser(UserRequest userRequest) {
        User user = new User();
        UserFromUserRequest(user, userRequest);
        userRepository.save(user);
    }



    public boolean updateUser(Long id, UserRequest userRequest) {
        return userRepository.findById(id)
                .map(
                        existingUser -> {
                           UserFromUserRequest(existingUser, userRequest);
                            userRepository.save(existingUser);
                            return true;


                        }
                ).orElse(false);
    }

    public boolean deleteUser(Long id) {
        boolean userExists = userRepository.existsById(id);
        if (userExists) {
            userRepository.deleteById(id);
        }
        return true;

    }

    private void UserFromUserRequest(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setPassword(userRequest.getPassword());

        if (userRequest.getAddress() != null) {
            Address address = new Address();
            address.setState(userRequest.getAddress().getState());
            address.setCity(userRequest.getAddress().getCity());
            address.setCountry(userRequest.getAddress().getCountry());
            address.setStreet(userRequest.getAddress().getStreet());
            address.setZip(userRequest.getAddress().getZip());
            user.setAddress(address);
        }

    }

    private UserResponse mapToUserResponse(User user) {

        UserResponse userResponse =  new UserResponse();
        userResponse.setId(String.valueOf(user.getId()));
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getRole());

        if (user.getAddress() != null) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setState(user.getAddress().getState());
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setZip(user.getAddress().getZip());
            addressDTO.setCountry(user.getAddress().getCountry());
            userResponse.setAddress(addressDTO);
        }
        userResponse.setCreatedAt(user.getCreatedAt());

        return userResponse;
    }

}
