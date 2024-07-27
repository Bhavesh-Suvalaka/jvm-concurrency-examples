package com.example.concurrency.services;

import com.example.concurrency.Utils;
import com.example.concurrency.models.CustomerPreference;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Response;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class PreferenceService {
  AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient();

  public PreferenceService() {
  }

  public List<CustomerPreference> fetchCustomerPreferences(String customerId) {
    Utils.sleep(400);
    log.info("%s".formatted(Thread.currentThread()));
    return Collections.emptyList();
  }

  public CompletableFuture<List<CustomerPreference>> fetchCustomerPreferencesAsync(String customerId) {
    return asyncHttpClient.prepareGet(STR."/customers/\{customerId}/preferences")
      .execute()
      .toCompletableFuture()
      .thenApplyAsync(this::toCustomerPreferences);
  }

  private List<CustomerPreference> toCustomerPreferences(Response response) {
    return Collections.emptyList();
  }
}
