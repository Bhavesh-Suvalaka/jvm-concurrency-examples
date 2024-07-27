package com.example.concurrency.services;

import com.example.concurrency.Utils;
import com.example.concurrency.external.client.CustomerClient;
import com.example.concurrency.models.Customer;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Response;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class CustomerService {
  private final CustomerClient customerClient;
  private AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient();

  public CustomerService(CustomerClient customerClient) {
    this.customerClient = customerClient;
  }

  public Optional<Customer> fetchCustomer(String customerId) {
    Utils.sleep(500);
    log.info("%s".formatted(Thread.currentThread()));
    return customerClient.getCustomer(customerId);
  }

  public CompletableFuture<Optional<Customer>> fetchCustomerAsync(String customerId) {
    return asyncHttpClient
      .prepareGet(STR."/customers/\{customerId}")
      .execute()
      .toCompletableFuture()
      .thenApplyAsync(this::toCustomer);
  }

  private Optional<Customer> toCustomer(Response response) {
    return Optional.of(new Customer("1", "Harry Potter", "411014"));
  }
}
