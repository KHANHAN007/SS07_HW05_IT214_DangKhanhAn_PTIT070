package com.finbank.loan.dto;

import java.math.BigDecimal;

public record LoanApplyResponse(
        Long loanId,
        String status,
        Long customerId,
        String customerName,
        String customerPhone,
        String disbursementAccountNumber,
        BigDecimal amount,
        Integer termMonths,
        BigDecimal annualInterestRate,
        BigDecimal monthlyPayment,
        String purpose,
        String message
) {
}

