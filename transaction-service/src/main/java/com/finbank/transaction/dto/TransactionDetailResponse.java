package com.finbank.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDetailResponse(
        Long transactionId,
        String status,
        String message,
        String fromAccountNumber,
        String fromCustomerName,
        String toAccountNumber,
        String toCustomerName,
        BigDecimal amount,
        String description,
        LocalDateTime createdAt
) {
}

