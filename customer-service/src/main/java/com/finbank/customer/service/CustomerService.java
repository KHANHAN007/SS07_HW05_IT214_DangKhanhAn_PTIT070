package com.finbank.customer.service;

import com.finbank.customer.dto.CustomerResponse;
import com.finbank.customer.model.Customer;
import com.finbank.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponse getByAccountNumber(String accountNumber) {
        Customer customer = customerRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay khach hang cua tai khoan " + accountNumber));
        return toResponse(customer);
    }

    public CustomerResponse getById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay khach hang " + id));
        return toResponse(customer);
    }

    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getAccountNumber(),
                customer.getFullName(),
                customer.getEmail(),
                customer.getPhone());
    }
}
