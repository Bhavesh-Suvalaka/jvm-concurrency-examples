package com.example.continuation;

import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * Run on JDK 21+ with <code>--add-opens java.base/jdk.internal.vm=ALL-UNNAMED --enable-preview</code>
 */
public class Example {
  public static void main(String[] args) {
    var continuation = new Continuation("counter", Example::increase);
    try (Scanner sc = new Scanner(System.in)) {
      while (!continuation.isDone()) {
        System.out.println("Press enter to print next count");
        sc.nextLine();
        continuation.run();
      }
    }
  }

  public static void count() {
    var continuation = new Continuation("counter", Example::increase);
    try (Scanner sc = new Scanner(System.in)) {
      while (!continuation.isDone()) {
        System.out.println("Press enter to print next count");
        sc.nextLine();
        continuation.run();
      }
    }
  }

  public static void increase(Continuation.Scope<Integer> scope) {
    IntStream.of(10).forEach(it -> Continuation.yield(scope, it));
  }
}
