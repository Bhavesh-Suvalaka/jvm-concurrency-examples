package com.example.fibers.services;

import com.example.fibers.models.Customer;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class CustomerService {
    private final ScheduledExecutorService scheduler;

    public CustomerService(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }

    public Optional<Customer> fetchCustomer(String customerId) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(new Customer(UUID.randomUUID(), "Jone doe", 411014));
    }

    public CompletableFuture<Optional<Customer>> fetchCustomerAsync(String customerId) {
        CompletableFuture<Optional<Customer>> delayedResult = new CompletableFuture<>();

        scheduler.schedule(
                () -> delayedResult.complete(Optional.of(new Customer(UUID.randomUUID(), "Jone doe", 411014))),
                500,
                TimeUnit.MILLISECONDS
        );

        return delayedResult;
    }
}
