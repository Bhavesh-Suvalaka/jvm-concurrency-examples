package com.example.concurrency;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class ThreadTest {
  public static void main(String[] args) {
    CompletableFuture.supplyAsync(() -> 0)
      .thenApply(i -> { log.info("stage 1: {}", i); return 1 / i; }) // executed and failed
      .thenApply(i -> { log.info("stage 2: {}", i); return 1 / i; }) // skipped
      .whenComplete((value, t) -> {
        if (t == null) {
          log.info("success: {}", value);
        } else {
          log.warn("failure: {}", t.getMessage()); // executed
        }
      })
      .thenApply(i -> { log.info("stage 3: {}", i); return 1 / i; }) // skipped
      .handle((value, t) -> {
        if (t == null) {
          return value + 1;
        } else {
          return -1; // executed and recovered
        }
      })
      .thenApply(i -> { log.info("stage 4: {}", i); return 1 / i; }) // executed
      .join();
  }
}