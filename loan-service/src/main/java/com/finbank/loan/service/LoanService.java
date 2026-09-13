package com.finbank.loan.service;

import com.finbank.loan.client.AccountServiceClient;
import com.finbank.loan.client.CustomerServiceClient;
import com.finbank.loan.dto.AccountResponse;
import com.finbank.loan.dto.CustomerResponse;
import com.finbank.loan.dto.LoanApplyRequest;
import com.finbank.loan.dto.LoanApplyResponse;
import com.finbank.loan.model.LoanApplication;
import com.finbank.loan.repository.LoanApplicationRepository;
import feign.FeignException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

    private static final BigDecimal ANNUAL_INTEREST_RATE = new BigDecimal("0.08");

    private final CustomerServiceClient customerServiceClient;
    private final AccountServiceClient accountServiceClient;
    private final LoanApplicationRepository loanApplicationRepository;

    public LoanService(CustomerServiceClient customerServiceClient,
                       AccountServiceClient accountServiceClient,
                       LoanApplicationRepository loanApplicationRepository) {
        this.customerServiceClient = customerServiceClient;
        this.accountServiceClient = accountServiceClient;
        this.loanApplicationRepository = loanApplicationRepository;
    }

    public LoanApplyResponse apply(LoanApplyRequest request) {
        validate(request);

        try {
            CustomerResponse customer = customerServiceClient.getById(request.customerId());
            AccountResponse activeAccount = accountServiceClient.getAccountsByCustomerId(request.customerId())
                    .stream()
                    .filter(AccountResponse::active)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("no active account"));

            BigDecimal monthlyPayment = calculateMonthlyPayment(request.amount(), request.termMonths());
            LoanApplication saved = loanApplicationRepository.save(new LoanApplication(
                    customer.id(),
                    customer.fullName(),
                    customer.phone(),
                    activeAccount.accountNumber(),
                    request.amount(),
                    request.termMonths(),
                    ANNUAL_INTEREST_RATE,
                    monthlyPayment,
                    request.purpose(),
                    "PENDING"));

            return toResponse(saved, "Dang ky khoan vay thanh cong, cho phe duyet");
        } catch (FeignException.NotFound ex) {
            throw new IllegalArgumentException("Khach hang khong ton tai");
        } catch (FeignException ex) {
            throw new IllegalArgumentException("Khong goi duoc service phu thuoc: " + ex.status());
        }
    }

    private void validate(LoanApplyRequest request) {
        if (request.customerId() == null) {
            throw new IllegalArgumentException("customerId khong duoc de trong");
        }
        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("So tien vay phai lon hon 0");
        }
        if (request.termMonths() == null || request.termMonths() <= 0) {
            throw new IllegalArgumentException("Ky han vay phai lon hon 0");
        }
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal amount, Integer termMonths) {
        BigDecimal years = BigDecimal.valueOf(termMonths).divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP);
        BigDecimal interest = amount.multiply(ANNUAL_INTEREST_RATE).multiply(years);
        return amount.add(interest).divide(BigDecimal.valueOf(termMonths), 2, RoundingMode.HALF_UP);
    }

    private LoanApplyResponse toResponse(LoanApplication loan, String message) {
        return new LoanApplyResponse(
                loan.getId(),
                loan.getStatus(),
                loan.getCustomerId(),
                loan.getCustomerName(),
                loan.getCustomerPhone(),
                loan.getDisbursementAccountNumber(),
                loan.getAmount(),
                loan.getTermMonths(),
                loan.getAnnualInterestRate(),
                loan.getMonthlyPayment(),
                loan.getPurpose(),
                message);
    }
}

