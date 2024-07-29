package com.example.concurrency.repo;

import com.example.concurrency.models.CustomerEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CassandraRepository {
  Long findByCustomerIdAndEventType(String customerId, String e1);
}
