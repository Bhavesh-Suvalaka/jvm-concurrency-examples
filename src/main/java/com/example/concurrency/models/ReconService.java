package com.example.concurrency.models;

import com.example.concurrency.repo.TransactionRepository;
import com.example.concurrency.services.PaymentTransactions;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@SuppressWarnings("preview")
@Slf4j
public class ReconService {
  private TransactionRepository repository;

  public void reconcileTransactions() throws InterruptedException {
    var reconInProgress = CompletableFuture.supplyAsync(
      this::reconcileInProgressTransactions
    );
    var reconAborted = CompletableFuture.supplyAsync(
      this::reconcileAbortedTransactions
    );
    var reconFailed = CompletableFuture.supplyAsync(
      this::reconcileFailedTransactions
    );

    reconInProgress
      .thenCombineAsync(reconAborted, Stream::of)
      .thenCombineAsync(reconFailed, Stream::of)
      .whenComplete((res, err) -> {
        if (err != null) handleError(err);
        repository.save(res.toList());
      })
      .join();
  }

  private void handleError(Throwable err) {
    log.error("Failed to reconcile ", err);
  }

  public CompletableFuture<List<PaymentTransactions>> reconcileInProgressTransactions() {
    // calls payment service to reconcile in progress transactions
    return CompletableFuture.completedFuture(Collections.emptyList());
  }

  public CompletableFuture<List<PaymentTransactions>> reconcileFailedTransactions() {
    // calls payment service to reconcile in progress transactions
    return CompletableFuture.completedFuture(Collections.emptyList());
  }

  public CompletableFuture<List<PaymentTransactions>> reconcileAbortedTransactions() {
    // calls payment service to reconcile in progress transactions
    return CompletableFuture.completedFuture(Collections.emptyList());
  }
}
