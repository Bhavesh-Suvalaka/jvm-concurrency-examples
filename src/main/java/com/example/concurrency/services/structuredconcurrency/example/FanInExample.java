package com.example.concurrency.services.structuredconcurrency.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.StructuredTaskScope;

public class FanInExample {

  void serve(ServerSocket serverSocket) throws IOException, InterruptedException {
    try (var scope = new StructuredTaskScope<Void>()) {
      try {
        while (true) {
          var socket = serverSocket.accept();
          scope.fork(() -> handle(socket));
        }
      } finally {
        System.out.println("Shutting down server");
        scope.shutdown();
        scope.join();
      }
    }
  }

  Void handle(Socket socket) throws IOException {
    return null;
  }
}
