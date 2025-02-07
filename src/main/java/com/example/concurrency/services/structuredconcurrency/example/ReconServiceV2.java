package com.example.concurrency.services.structuredconcurrency.example;

import com.example.concurrency.repo.TransactionRepository;
import com.example.concurrency.models.PaymentTransactions;
import com.example.config.CustomTaskScope;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.StructuredTaskScope.Subtask;

@SuppressWarnings("preview")
@Slf4j
public class ReconServiceV2 {
  private TransactionRepository repository;

  public void reconcileTransactions() throws InterruptedException {
    try (var scope = new CustomTaskScope<List<PaymentTransactions>>()) {
      scope.fork(this::reconcileAbortedTransactions);
      scope.fork(this::reconcileInProgressTransactions);
      scope.fork(this::reconcileAbortedTransactions);

      var successFullTransactions = scope.join().successfulTasks()
        .map(Subtask::get).toList();
      repository.save(successFullTransactions);
      scope.join().failedTasks().forEach(it -> log.error("Failed to reconcile {}", it.get()));
    }
  }

  public List<PaymentTransactions> reconcileInProgressTransactions() {
    // calls payment service to reconcile in progress transactions
    return Collections.emptyList();
  }

  public List<PaymentTransactions> reconcileFailedTransactions() {
    // calls payment service to reconcile in progress transactions
    return Collections.emptyList();
  }

  public List<PaymentTransactions> reconcileAbortedTransactions() {
    // calls payment service to reconcile in progress transactions
    return Collections.emptyList();
  }
}
