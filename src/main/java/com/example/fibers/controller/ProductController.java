package com.example.fibers.controller;

import com.example.fibers.dto.ProductDto;
import com.example.fibers.models.Product;
import com.example.fibers.services.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public void addProducts(@RequestBody ProductDto product) {
        productService.addProduct(product);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable UUID id) {
        return productService.getProductBy(id);
    }
}
