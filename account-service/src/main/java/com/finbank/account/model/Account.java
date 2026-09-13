package com.finbank.account.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long customerId;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    private String ownerName;

    @Column(nullable = false)
    private BigDecimal balance;

    private boolean active;

    public Account() {
    }

    public Account(Long id, Long customerId, String accountNumber, String ownerName, BigDecimal balance, boolean active) {
        this.id = id;
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
