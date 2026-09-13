package com.finbank.transaction.service;

import com.finbank.transaction.client.AccountServiceClient;
import com.finbank.transaction.client.CustomerServiceClient;
import com.finbank.transaction.dto.AmountRequest;
import com.finbank.transaction.dto.CustomerResponse;
import com.finbank.transaction.dto.TransactionDetailResponse;
import com.finbank.transaction.dto.TransferRequest;
import com.finbank.transaction.dto.TransferResponse;
import com.finbank.transaction.model.TransactionRecord;
import com.finbank.transaction.repository.TransactionRepository;
import feign.FeignException;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    private final AccountServiceClient accountServiceClient;
    private final CustomerServiceClient customerServiceClient;
    private final TransactionRepository transactionRepository;

    public TransferService(
            AccountServiceClient accountServiceClient,
            CustomerServiceClient customerServiceClient,
            TransactionRepository transactionRepository) {
        this.accountServiceClient = accountServiceClient;
        this.customerServiceClient = customerServiceClient;
        this.transactionRepository = transactionRepository;
    }

    public TransferResponse transfer(TransferRequest request) {
        try {
            validateRequest(request);
            accountServiceClient.getAccount(request.fromAccountNumber());
            accountServiceClient.getAccount(request.toAccountNumber());

            BigDecimal balance = accountServiceClient.getBalance(request.fromAccountNumber()).balance();
            if (balance.compareTo(request.amount()) < 0) {
                return saveFailed(request, "So du khong du");
            }

            AmountRequest amountRequest = new AmountRequest(request.amount());
            accountServiceClient.debit(request.fromAccountNumber(), amountRequest);
            accountServiceClient.credit(request.toAccountNumber(), amountRequest);

            TransactionRecord saved = transactionRepository.save(new TransactionRecord(
                    request.fromAccountNumber(),
                    request.toAccountNumber(),
                    request.amount(),
                    request.description(),
                    "SUCCESS",
                    "Chuyen tien thanh cong"));

            return toResponse(saved);
        } catch (IllegalArgumentException | FeignException ex) {
            return saveFailed(request, ex.getMessage());
        }
    }

    public TransactionDetailResponse getDetail(Long id) {
        TransactionRecord transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay giao dich " + id));

        CustomerResponse fromCustomer = customerServiceClient.getByAccountNumber(transaction.getFromAccountNumber());
        CustomerResponse toCustomer = customerServiceClient.getByAccountNumber(transaction.getToAccountNumber());

        return new TransactionDetailResponse(
                transaction.getId(),
                transaction.getStatus(),
                transaction.getMessage(),
                transaction.getFromAccountNumber(),
                fromCustomer.fullName(),
                transaction.getToAccountNumber(),
                toCustomer.fullName(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getCreatedAt());
    }

    private void validateRequest(TransferRequest request) {
        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("So tien phai lon hon 0");
        }
        if (request.fromAccountNumber() == null || request.toAccountNumber() == null) {
            throw new IllegalArgumentException("Tai khoan nguon va dich khong duoc de trong");
        }
    }

    private TransferResponse saveFailed(TransferRequest request, String message) {
        TransactionRecord saved = transactionRepository.save(new TransactionRecord(
                request.fromAccountNumber(),
                request.toAccountNumber(),
                request.amount(),
                request.description(),
                "FAILED",
                message));
        return toResponse(saved);
    }

    private TransferResponse toResponse(TransactionRecord record) {
        return new TransferResponse(
                record.getId(),
                record.getStatus(),
                record.getMessage(),
                record.getFromAccountNumber(),
                record.getToAccountNumber(),
                record.getAmount());
    }
}
