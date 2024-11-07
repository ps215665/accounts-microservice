package com.ps.accounts.services;

import com.ps.accounts.dto.CustomerDto;

public interface AccountServiceInterface {

    /**
     *
     * @param customerDto - CustomerDto Object
     */
    void createAccount(CustomerDto customerDto);

    /**
     *
     * @param mobileNumber - Fetch account by mobile number
     * @return CustomerDto
     */
    CustomerDto fetchAccount(String mobileNumber);

    /**
     *
     * @param customerDto - CustomerDto Object
     * @return boolean
     */
    boolean updateAccount(CustomerDto customerDto);

    /**
     *
     * @param mobileNumber - Mobile Number
     * @return boolean
     */
    boolean deleteAccount(String mobileNumber);
}
