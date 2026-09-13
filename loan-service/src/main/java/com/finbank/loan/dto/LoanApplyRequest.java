package com.finbank.loan.dto;

import java.math.BigDecimal;

public record LoanApplyRequest(
        Long customerId,
        BigDecimal amount,
        Integer termMonths,
        String purpose
) {
}

