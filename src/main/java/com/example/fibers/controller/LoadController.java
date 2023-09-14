package com.example.fibers.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoadController {
    private final Logger log = LoggerFactory.getLogger(LoadController.class);

    @GetMapping("/load")
    void testingLoad() throws InterruptedException {
        log.info("processing a request");
        Thread.sleep(1000);
    }
}
