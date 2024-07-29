package com.example.concurrency;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.Supplier;

public class ThreadDumpExample {

  static Long sleepOneSecond(String s) throws InterruptedException {
    long pid = ProcessHandle.current().pid();
    for (int i = 0; i < 60; i++) {
      System.out.println(STR."[\{pid}, \{s}] Sleeping for 1s...");
      Thread.sleep(1000);
    }
    return Long.valueOf(pid);
  }

  void handle() throws ExecutionException, InterruptedException {
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
      Supplier<Long> task1 = scope.fork(() -> sleepOneSecond("task1"));
      Supplier<Long> task2 = scope.fork(() -> sleepOneSecond("task2"));
      Supplier<Long> task3 = scope.fork(() -> sleepOneSecond("task3"));
      scope.join()
        .throwIfFailed();
    }
  }

  public static void main(String[] args) {
    try {
      var myApp = new ThreadDumpExample();
      myApp.handle();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
