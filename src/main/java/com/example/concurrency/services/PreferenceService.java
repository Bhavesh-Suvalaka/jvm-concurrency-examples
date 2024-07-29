package com.example.concurrency.services;

import com.example.concurrency.Utils;
import com.example.concurrency.models.CustomerPreference;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PreferenceService {
  private final ScheduledExecutorService scheduler;

  public PreferenceService(AsyncHttpClient asyncHttpClient, ScheduledExecutorService scheduler) {
    this.scheduler = scheduler;
  }

  public List<CustomerPreference> fetchCustomerPreferences(String customerId) {
    Utils.sleep(400);
    log.info("%s".formatted(Thread.currentThread()));
    return Collections.emptyList();
  }

  public CompletableFuture<List<CustomerPreference>> fetchCustomerPreferencesAsync(String customerId) {
    CompletableFuture<List<CustomerPreference>> delayedResult = new CompletableFuture<>();

    scheduler.schedule(
      () -> delayedResult.complete(Collections.emptyList()),
      400,
      TimeUnit.MILLISECONDS
    );

    return delayedResult;
  }

  private List<CustomerPreference> toCustomerPreferences(Response response) {
    return Collections.emptyList();
  }
}
