package com.example.concurrency.client;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.concurrent.CompletableFuture;

public class HttpClient {
  public <T> CompletableFuture<T> asyncPost(String baseUrl, Object requestBody, TypeReference<T> type) {
    return CompletableFuture.completedFuture((T) new Object());
  }

  public <T> T post(String baseUrl, Object requestBody, TypeReference<T> type) {
    return (T) new Object();
  }
}
