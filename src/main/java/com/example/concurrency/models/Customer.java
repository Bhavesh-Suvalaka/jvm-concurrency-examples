package com.example.concurrency.models;

public record Customer(
  String customerId,
  String name,
  String pincode
) {
}
