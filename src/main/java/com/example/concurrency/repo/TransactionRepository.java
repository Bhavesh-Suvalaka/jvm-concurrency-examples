package com.example.concurrency.repo;

import com.example.concurrency.services.PaymentTransactions;

import java.util.List;

public interface TransactionRepository {
  void save(List successFullTransactions);
}
