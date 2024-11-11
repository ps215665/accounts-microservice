package com.ps.accounts.services.impl;

import com.ps.accounts.dto.AccountsDto;
import com.ps.accounts.dto.CardsDto;
import com.ps.accounts.dto.CustomerDetailsDto;
import com.ps.accounts.dto.LoansDto;
import com.ps.accounts.entities.Accounts;
import com.ps.accounts.entities.Customer;
import com.ps.accounts.exceptions.ResourceNotFoundException;
import com.ps.accounts.mapper.AccountsMapper;
import com.ps.accounts.mapper.CustomerMapper;
import com.ps.accounts.repositories.AccountsRepository;
import com.ps.accounts.repositories.CustomerRepository;
import com.ps.accounts.services.CustomerServiceInterface;
import com.ps.accounts.services.client.CardsFeignClient;
import com.ps.accounts.services.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerServiceInterface {

    private final AccountsRepository accountsRepository;
    private final CustomerRepository customerRepository;
    private final CardsFeignClient cardsFeignClient;
    private final LoansFeignClient loansFeignClient;

    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findById(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccount(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoan(mobileNumber);
        customerDetailsDto.setLoans(loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber);
        customerDetailsDto.setCards(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;
    }
}