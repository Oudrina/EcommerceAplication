package com.Audrina.ecommerce.controller;

import com.Audrina.ecommerce.dto.UserRequest;
import com.Audrina.ecommerce.dto.UserResponse;
import com.Audrina.ecommerce.model.User;
import com.Audrina.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return new ResponseEntity<>(userService.fetchAllUser(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return userService.fetchUser(id).map(
                        user ->
                                new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(
                        new ResponseEntity<>(HttpStatus.NOT_FOUND));


    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {
        userService.createUser(userRequest);
        return new ResponseEntity<>("User created Successfully", HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        boolean updatedUser = userService.updateUser(id, userRequest);

        return updatedUser ?
                new ResponseEntity<>("User updated successfully", HttpStatus.OK)
                :
                new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);


    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        boolean deletedUser = userService.deleteUser(id);
        return deletedUser ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT)
                :
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

}

