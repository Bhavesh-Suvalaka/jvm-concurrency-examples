package com.example.recommendation.models;

import java.util.UUID;

public record CustomerPreference(
        UUID customerId,
        String preferredItemCategory
) {
}