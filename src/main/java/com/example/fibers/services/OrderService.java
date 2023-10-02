package com.example.fibers.services;

import com.example.fibers.models.Order;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
public class OrderService {
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
}
