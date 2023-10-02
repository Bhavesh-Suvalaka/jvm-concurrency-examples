package com.example.fibers.models;

import java.util.UUID;

public record Customer(
        UUID customerId,
        String name,
        int pincode
) {
}
