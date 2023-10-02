package com.example.fibers.models;

import java.util.UUID;

public record CustomerPreference(
        UUID customerId,
        String preferredItemCategory
) {
}