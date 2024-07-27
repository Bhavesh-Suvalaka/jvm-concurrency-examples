package com.example.concurrency.models;

import java.util.UUID;

public record CustomerPreference(
  UUID customerId,
  String preferredItemCategory
) {
}