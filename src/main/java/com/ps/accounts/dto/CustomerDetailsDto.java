package com.ps.accounts.dto;

import lombok.Data;

@Data
public class CustomerDetailsDto {

    private String name;
    private String email;
    private String mobileNumber;
    private AccountsDto account;
    private LoansDto loans;
    private CardsDto cards;
}