package com.Audrina.ecommerce.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UserRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;
    private AddressDTO address;
}
