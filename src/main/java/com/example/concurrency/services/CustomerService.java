package com.example.concurrency.services;

import com.example.concurrency.Utils;
import com.example.concurrency.external.client.CustomerClient;
import com.example.concurrency.models.Customer;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.Response;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CustomerService {
  private final CustomerClient customerClient;
  private final ScheduledExecutorService scheduler;

  public CustomerService(CustomerClient customerClient, ScheduledExecutorService scheduler) {
    this.customerClient = customerClient;
    this.scheduler = scheduler;
  }

  public Optional<Customer> fetchCustomer(String customerId) {
    Utils.sleep(500);
    log.info("%s".formatted(Thread.currentThread()));
    return customerClient.getCustomer(customerId);
  }

  public CompletableFuture<Optional<Customer>> fetchCustomerAsync(String customerId) {
    CompletableFuture<Optional<Customer>> delayedResult = new CompletableFuture<>();

    scheduler.schedule(
      () -> delayedResult.complete(Optional.of(new Customer("1", "Jone doe", "411014"))),
      500,
      TimeUnit.MILLISECONDS
    );

    return delayedResult;
  }

  private Optional<Customer> toCustomer(Response response) {
    return Optional.of(new Customer("1", "Harry Potter", "411014"));
  }
}
