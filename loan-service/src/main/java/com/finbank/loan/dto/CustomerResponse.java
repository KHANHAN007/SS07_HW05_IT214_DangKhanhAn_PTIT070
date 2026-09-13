package com.finbank.loan.dto;

public record CustomerResponse(
        Long id,
        String accountNumber,
        String fullName,
        String email,
        String phone
) {
}

