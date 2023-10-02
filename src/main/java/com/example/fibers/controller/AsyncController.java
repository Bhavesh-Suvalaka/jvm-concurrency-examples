package com.example.fibers.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.random.RandomGenerator;

@RestController
public class AsyncController {
    private final Logger log = LoggerFactory.getLogger(AsyncController.class);

    @GetMapping("/async/load")
    void testingLoad() throws InterruptedException {
        log.info("processing a request");
        Thread.sleep(1000);
    }

    CompletableFuture<Integer> getTick() throws InterruptedException {
        Thread.sleep(500);
        return CompletableFuture.supplyAsync(() -> RandomGenerator.getDefault().nextInt());
    }
}
