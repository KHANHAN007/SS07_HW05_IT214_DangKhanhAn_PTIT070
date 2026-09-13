package com.finbank.loan.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private String customerName;
    private String customerPhone;
    private String disbursementAccountNumber;
    private BigDecimal amount;
    private Integer termMonths;
    private BigDecimal annualInterestRate;
    private BigDecimal monthlyPayment;
    private String purpose;
    private String status;
    private LocalDateTime createdAt;

    public LoanApplication() {
    }

    public LoanApplication(Long customerId, String customerName, String customerPhone,
                           String disbursementAccountNumber, BigDecimal amount, Integer termMonths,
                           BigDecimal annualInterestRate, BigDecimal monthlyPayment,
                           String purpose, String status) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.disbursementAccountNumber = disbursementAccountNumber;
        this.amount = amount;
        this.termMonths = termMonths;
        this.annualInterestRate = annualInterestRate;
        this.monthlyPayment = monthlyPayment;
        this.purpose = purpose;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public String getDisbursementAccountNumber() {
        return disbursementAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public BigDecimal getAnnualInterestRate() {
        return annualInterestRate;
    }

    public BigDecimal getMonthlyPayment() {
        return monthlyPayment;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getStatus() {
        return status;
    }
}

