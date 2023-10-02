package com.example.fibers.models;

import java.util.List;
import java.util.UUID;

public record Order(
        UUID id,
        List<Product> productList
) {}
