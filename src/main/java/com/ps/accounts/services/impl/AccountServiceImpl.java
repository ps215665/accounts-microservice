package com.ps.accounts.services.impl;

import com.ps.accounts.constants.AccountConstants;
import com.ps.accounts.dto.AccountsDto;
import com.ps.accounts.dto.CustomerDto;
import com.ps.accounts.entities.Accounts;
import com.ps.accounts.entities.Customer;
import com.ps.accounts.exceptions.CustomerAlreadyExistsException;
import com.ps.accounts.exceptions.ResourceNotFoundException;
import com.ps.accounts.mapper.AccountsMapper;
import com.ps.accounts.mapper.CustomerMapper;
import com.ps.accounts.repositories.AccountsRepository;
import com.ps.accounts.repositories.CustomerRepository;
import com.ps.accounts.services.AccountServiceInterface;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountServiceInterface {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    /**
     * @param customerDto - CustomerDto Object
     */
    @Override
    public void createAccount(CustomerDto customerDto) {

        if (customerRepository.existsByMobileNumberOrEmail(
                String.valueOf(customerDto.getMobileNumber()),
                String.valueOf(customerDto.getEmail())
        )) {
            throw new CustomerAlreadyExistsException("Customer already exists with the given mobile number or email");
        }

        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Customer savedCustomer = customerRepository.save(customer);

        accountsRepository.save(createNewAccount(savedCustomer));
    }

    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomer(customer);

        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountConstants.TYPE_SAVINGS);
        newAccount.setBranchAddress(AccountConstants.ADDRESS);
        return newAccount;
    }

    /**
     * @param mobileNumber - Fetch account by mobile number
     * @return CustomerDto
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {

        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Customer",
                        "mobileNumber",
                        mobileNumber
                )
        );

        AccountsDto accountsDto = AccountsMapper.mapToAccountsDto(customer.getAccount(), new AccountsDto());
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccount(accountsDto);
        return customerDto;
    }

    /**
     * @param customerDto - CustomerDto Object
     * @return boolean
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;

        AccountsDto accountsDto = customerDto.getAccount();

        if (accountsDto != null) {
            Accounts accounts = accountsRepository.findByAccountNumber(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "Account Number", accountsDto.getAccountNumber().toString())
            );

            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomer().getCustomerId();

            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "Customer Id", customerId.toString())
            );

            CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(customer);
            isUpdated = true;
        }

        return isUpdated;
    }

    /**
     * @param mobileNumber - Mobile Number
     * @return boolean
     */
    @Override
    @Transactional
    @Modifying //Denotes that this function will modify data so run the sql statements in a transaction
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Account", "MobileNumber", mobileNumber)
        );

        customerRepository.delete(customer);

        return true;
    }
}