package com.ps.accounts.services;

import com.ps.accounts.dto.CustomerDetailsDto;

public interface CustomerServiceInterface {

    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber);
}
