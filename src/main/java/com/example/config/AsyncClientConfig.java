package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class AsyncClientConfig {

    @Bean
    public ScheduledExecutorService scheduledExecutor() {
        return Executors.newScheduledThreadPool(100);
    }
}
