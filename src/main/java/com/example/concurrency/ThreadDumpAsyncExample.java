package com.example.concurrency;

import java.util.concurrent.CompletableFuture;

public class ThreadDumpAsyncExample {

  static CompletableFuture<Long> cuboidVolume(String s) {
    return getLength()
      .thenCombineAsync(getBreadth(), (l, b) -> l * b)
      .thenCombineAsync(getHeight(), (lb, h) -> lb * h);
  }

  private static CompletableFuture<Long> getLength() {
    return CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return 10L;
    });
  }

  private static CompletableFuture<Long> getBreadth() {
    return CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return 5L;
    });
  }

  private static CompletableFuture<Long> getHeight() {
    return CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return 8L;
    });
  }

  void handle(String s) throws InterruptedException {
    long pid = ProcessHandle.current().pid();
    System.out.println(STR."[\{pid}, \{s}] Sleeping for 1s...");
    Thread.sleep(5000);

    cuboidVolume("t1").join();
  }

  public static void main(String[] args) {
    try {
      var myApp = new ThreadDumpAsyncExample();
      myApp.handle("task1");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
