package com.finbank.loan.dto;

import java.math.BigDecimal;

public record AccountResponse(
        Long customerId,
        String accountNumber,
        String ownerName,
        BigDecimal balance,
        boolean active
) {
}

