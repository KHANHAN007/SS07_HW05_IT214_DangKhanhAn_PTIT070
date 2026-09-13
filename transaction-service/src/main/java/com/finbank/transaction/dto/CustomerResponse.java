package com.finbank.transaction.dto;

public record CustomerResponse(
        Long id,
        String accountNumber,
        String fullName,
        String email,
        String phone
) {
}
