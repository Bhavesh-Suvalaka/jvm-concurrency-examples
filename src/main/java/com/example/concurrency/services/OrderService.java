package com.example.concurrency.services;

import com.example.concurrency.Utils;
import com.example.concurrency.models.Order;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Response;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.IntStream;

@Service
@Slf4j
public class OrderService {
  private final ScheduledExecutorService scheduler;
  private final AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient();

  public OrderService(ScheduledExecutorService scheduler) {
    this.scheduler = scheduler;
  }

  public List<Order> fetchOrderHistory(String customerId) {
    Utils.sleep(1000);
    log.error("%s".formatted(Thread.currentThread()));
    return IntStream.range(1, 10).mapToObj(it ->
      new Order(UUID.randomUUID(), Collections.emptyList())
    ).toList();
  }

  public CompletableFuture<List<Order>> fetchOrderHistoryAsync(String customerId) {
    return asyncHttpClient
      .prepareGet(STR."/orders/customers/\{customerId}")
      .execute()
      .toCompletableFuture()
      .thenApplyAsync(this::toOrders);
  }

  private List<Order> toOrders(Response response) {
    return List.of();
  }
}
