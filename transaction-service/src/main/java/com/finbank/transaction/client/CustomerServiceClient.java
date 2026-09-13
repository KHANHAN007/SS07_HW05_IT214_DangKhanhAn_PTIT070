package com.finbank.transaction.client;

import com.finbank.transaction.dto.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service")
public interface CustomerServiceClient {

    @GetMapping("/api/customers/by-account/{accountNumber}")
    CustomerResponse getByAccountNumber(@PathVariable("accountNumber") String accountNumber);
}

