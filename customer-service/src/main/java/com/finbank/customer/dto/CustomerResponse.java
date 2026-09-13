package com.finbank.customer.dto;

public record CustomerResponse(
        Long id,
        String accountNumber,
        String fullName,
        String email,
        String phone
) {
}
