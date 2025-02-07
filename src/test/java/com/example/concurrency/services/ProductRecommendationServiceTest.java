package com.example.concurrency.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductRecommendationServiceTest {
  @Autowired
  private ProductRecommendationService productRecommendationService;

  @Test
  void timeTakenUsingThreadsConcurrentExecution() throws Exception {
    ExecutionTimer.measure(() -> productRecommendationService.recommendProductsT1("123"));
  }

  @Test
  void timeTakenForBlockingOperation() throws Exception {
    ExecutionTimer.measure(() -> productRecommendationService.recommendProducts("123"));
  }

  @Test
  void timeTakenForThreadPool() throws Exception {
    ExecutionTimer.measure(() -> productRecommendationService.recommendProductsThreadPool("123"));
  }

  @Test
  void timeTakenForForkJoinPool() throws Exception {
    ExecutionTimer.measure(() -> productRecommendationService.recommendProductsForkJoinPool("123"));
  }
}