package com.example.concurrency.controller;

import com.example.concurrency.models.Product;
import com.example.concurrency.services.ProductRecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/v2/recommended-products")
public class RecommendProductAsyncController {
  private final ProductRecommendationService productRecommendationService;

  public RecommendProductAsyncController(ProductRecommendationService productRecommendationService) {
    this.productRecommendationService = productRecommendationService;
  }

  @GetMapping("/customer/{customerId}")
  public CompletableFuture<List<Product>> recommendedProducts(@PathVariable("customerId") String customerId) throws ExecutionException, InterruptedException {
    return productRecommendationService.recommendProductsAsync(customerId);
  }
}
