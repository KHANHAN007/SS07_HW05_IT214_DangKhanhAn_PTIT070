package com.finbank.customer;

import com.finbank.customer.model.Customer;
import com.finbank.customer.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner seedCustomers(CustomerRepository customerRepository) {
        return args -> {
            if (customerRepository.count() == 0) {
                customerRepository.save(new Customer(null, "1001", "Nguyen Van A", "nguyenvana@finbank.vn", "0901001001"));
                customerRepository.save(new Customer(null, "1002", "Tran Thi B", "tranthib@finbank.vn", "0902002002"));
                customerRepository.save(new Customer(null, "9999", "Le Khong Co TK", "noaccount@finbank.vn", "0909999999"));
            }
        };
    }
}
