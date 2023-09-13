package com.example.fibers.services;

import com.example.fibers.dto.ProductDto;
import com.example.fibers.models.Product;
import com.example.fibers.repo.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void addProduct(ProductDto productDto) {
        Product product = Product.from(productDto);
        productRepository.save(product);
    }

    public Product getProductBy(UUID id) {
        return productRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Product not found for id: " + id)
        );
    }
}
