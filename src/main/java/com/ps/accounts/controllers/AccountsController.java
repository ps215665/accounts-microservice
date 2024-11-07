package com.ps.accounts.controllers;

import com.ps.accounts.constants.AccountConstants;
import com.ps.accounts.dto.CustomerDto;
import com.ps.accounts.dto.ResponseDto;
import com.ps.accounts.services.AccountServiceInterface;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class AccountsController {

    private final AccountServiceInterface accountService;

    public AccountsController(AccountServiceInterface accountService) {
        this.accountService = accountService;
    }

    @Value("${build.version}")
    private String buildVersion;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {

        accountService.createAccount(customerDto);

        return ResponseEntity
                .status(HttpStatus.CREATED) // this will be set in the response header
                .body(new ResponseDto(
                        AccountConstants.STATUS_201,
                        AccountConstants.MESSAGE_201
                ));
    }

    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccount(@RequestParam String mobileNumber)
    {
        CustomerDto customerDtp = accountService.fetchAccount(mobileNumber);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerDtp);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccount(@RequestBody CustomerDto customerDto) {
        boolean isAccountUpdated = accountService.updateAccount(customerDto);

        if (isAccountUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(
                            AccountConstants.STATUS_200,
                            AccountConstants.MESSAGE_200
                    ));
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(
                            AccountConstants.STATUS_500,
                            AccountConstants.MESSAGE_500
                    ));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccount(@RequestParam String mobileNumber)
    {
        accountService.deleteAccount(mobileNumber);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(
                        AccountConstants.STATUS_200,
                        AccountConstants.MESSAGE_200
                ));
    }

    @GetMapping("/version")
    public ResponseEntity<Map<String, String>> buildVersion()
    {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        Map.of("version", buildVersion)
                );
    }
}
