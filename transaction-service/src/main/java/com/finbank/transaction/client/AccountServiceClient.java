package com.finbank.transaction.client;

import com.finbank.transaction.dto.AccountResponse;
import com.finbank.transaction.dto.AmountRequest;
import com.finbank.transaction.dto.BalanceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "account-service")
public interface AccountServiceClient {

    @GetMapping("/api/accounts/{accountNumber}")
    AccountResponse getAccount(@PathVariable("accountNumber") String accountNumber);

    @GetMapping("/api/accounts/{accountNumber}/balance")
    BalanceResponse getBalance(@PathVariable("accountNumber") String accountNumber);

    @PutMapping("/api/accounts/{accountNumber}/debit")
    AccountResponse debit(@PathVariable("accountNumber") String accountNumber, @RequestBody AmountRequest request);

    @PutMapping("/api/accounts/{accountNumber}/credit")
    AccountResponse credit(@PathVariable("accountNumber") String accountNumber, @RequestBody AmountRequest request);
}

