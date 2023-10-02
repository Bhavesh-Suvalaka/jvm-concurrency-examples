package com.example.fibers.services;

import com.example.fibers.models.Order;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Service
public class OrderService {
    private final ScheduledExecutorService scheduler;

    public OrderService(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }

    public List<Order> fetchOrderHistory(String customerId) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return IntStream.range(1, 10).mapToObj(it ->
                new Order(UUID.randomUUID(), Collections.emptyList())
        ).toList();
    }

    public CompletableFuture<List<Order>> fetchOrderHistoryAsync(String customerId) {
        CompletableFuture<List<Order>> delayedResult = new CompletableFuture<>();
        List<Order> orders = IntStream.range(1, 10).mapToObj(it ->
                new Order(UUID.randomUUID(), Collections.emptyList())
        ).toList();

        scheduler.schedule(
                () -> delayedResult.complete(orders),
                1000,
                TimeUnit.MILLISECONDS
        );

        return delayedResult;
    }
}
