package com.example.fibers.services;

import com.example.fibers.models.Customer;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {
    public Optional<Customer> fetchCustomer(String customerId) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(new Customer(UUID.randomUUID(), "Jone doe", 411014));
    }
}
