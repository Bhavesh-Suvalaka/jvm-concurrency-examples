package com.example.recommendation.controller;

import com.example.recommendation.models.Product;
import com.example.recommendation.services.ProductRecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/v2/recommended-products")
public class RecommendProductAsyncController {
    private final ProductRecommendationService productRecommendationService;

    public RecommendProductAsyncController(ProductRecommendationService productRecommendationService) {
        this.productRecommendationService = productRecommendationService;
    }

    @GetMapping("/customer/{customerId}")
    public CompletableFuture<List<Product>> recommendedProducts(@PathVariable("customerId") String customerId) {
        return productRecommendationService.recommendProductsAsync(customerId);
    }
}
