package com.ps.accounts.repositories;

import com.ps.accounts.dto.CustomerDto;
import com.ps.accounts.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByMobileNumberOrEmail(String mobileNumber, String email);

    Optional<Customer> findByMobileNumber(String mobileNumber);
}
