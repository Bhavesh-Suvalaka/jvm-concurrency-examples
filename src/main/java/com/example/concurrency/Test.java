package com.example.concurrency;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Test {
  public static void main(String[] args) throws IOException {
    int number = 10;
    for (int i = 0; i < number; i++) {
      System.out.println(square(i));
    }

  }

  private static int square(int i) {
    return i * i;
  }
}
