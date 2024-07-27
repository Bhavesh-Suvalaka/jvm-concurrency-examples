package com.example.concurrency.services;

import com.example.concurrency.client.HttpClient;
import com.example.concurrency.models.VehicleData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.stream.Stream;

@SuppressWarnings("preview")
public class VehicleService {
  private final String baseUrl;
  private final HttpClient httpClient;
  private final TypeReference<List<VehicleData>> typeReference = new TypeReference<>() {
  };

  public VehicleService(String baseUrl, HttpClient httpClient) {
    this.baseUrl = baseUrl;
    this.httpClient = httpClient;
  }

  public CompletableFuture<List<VehicleData>> getVehicleData(List<String> vehicleIds) {
    List<CompletableFuture<List<VehicleData>>> vehicleDataFutures = Lists.partition(vehicleIds, 100)
      .stream().map(it ->
        httpClient.asyncPost(baseUrl, it, typeReference)
      ).toList();

    return CompletableFuture.allOf(
      vehicleDataFutures.toArray(CompletableFuture[]::new)
    ).thenApplyAsync(_ -> vehicleDataFutures.stream()
      .map(CompletableFuture::join)
      .flatMap(List::stream)
      .toList());
  }

  public List<VehicleData> getVehicleDataV2(List<String> vehicleIds) throws InterruptedException {
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
      Stream<Subtask<List<VehicleData>>> subtask = Lists.partition(vehicleIds, 100)
        .stream().map(it ->
          scope.fork(() -> httpClient.post(baseUrl, it, typeReference))
        );
      scope.join().throwIfFailed(RuntimeException::new);

      return subtask.map(Subtask::get)
        .flatMap(List::stream)
        .toList();
    }
  }
}
