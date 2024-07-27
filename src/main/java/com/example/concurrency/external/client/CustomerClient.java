package com.example.concurrency.external.client;

import com.example.concurrency.models.Customer;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerClient {
  public Optional<Customer> getCustomer(String customerId) {
    return Optional.of(new Customer(customerId, "Jon Doe", "007"));
  }
}
