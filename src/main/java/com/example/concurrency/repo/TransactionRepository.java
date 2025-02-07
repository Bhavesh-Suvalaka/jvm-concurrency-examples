package com.example.concurrency.repo;

import java.util.List;

public interface TransactionRepository {
  void save(List successFullTransactions);
}
