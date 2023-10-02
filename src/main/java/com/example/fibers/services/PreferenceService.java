package com.example.fibers.services;

import com.example.fibers.models.CustomerPreference;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class PreferenceService {
    private final ScheduledExecutorService scheduler;

    public PreferenceService(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }

    public List<CustomerPreference> fetchCustomerPreferences(String customerId) {
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
}
