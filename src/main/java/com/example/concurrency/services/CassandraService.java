package com.example.concurrency.services;

import com.example.concurrency.repo.CassandraRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.stream.Stream;

@SuppressWarnings("ALL")
@Slf4j
public class CassandraService {
  private final CassandraRepository cassandraRepository;
  private final static Set<String> eventTypes = Set.of("E1", "E2", "E3");
  private final ExecutorService executor = Executors.newWorkStealingPool();

  public CassandraService(CassandraRepository cassandraRepository) {
    this.cassandraRepository = cassandraRepository;
  }

  public CompletableFuture<Long> getEventsCount(List<String> customerIds) {
    var counts = customerIds
      .stream()
      .map(it ->
        CompletableFuture.supplyAsync(() -> getEventCountForEvent(it, eventTypes), executor)
      ).toList();

    return CompletableFuture.allOf(counts.toArray(CompletableFuture[]::new))
      .exceptionally(e -> {
        log.error("error: ", e);
        return null;
      })
      .thenApplyAsync(_ ->
        counts.stream().flatMap(CompletableFuture::join)
          .reduce(0L, Long::sum)
      );
  }

  public Long getEventsCount2(List<String> customerIds) {
    try (var scope = new StructuredTaskScope<Stream<Long>>()) {
      var subsTasks = customerIds
        .stream()
        .map(it -> scope.fork(() -> getEventCountForEvent(it, eventTypes)));

      scope.join();

      return subsTasks
        .flatMap(Subtask::get)
        .reduce(0L, Long::sum);

    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private Stream<Long> getEventCountForEvent(String customerId, Set<String> eventTypes) {
    return eventTypes
      .stream()
      .map(event -> cassandraRepository.findByCustomerIdAndEventType(customerId, event))
      ;
  }
}
