package com.ps.accounts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerDto {

    @NotEmpty(message = "Name cannot be empty or null")
    @Size(min = 3, max = 30)
    private String name;

    @NotEmpty(message = "Email cannot be empty or null")
    @Email(message = "Please enter a valid email")
    private String email;


    private String mobileNumber;
    private AccountsDto account;
}
