package com.example.recommendation.services;

import com.example.recommendation.models.Customer;
import com.example.recommendation.models.CustomerPreference;
import com.example.recommendation.models.Order;
import com.example.recommendation.models.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.StructuredTaskScope;

@SuppressWarnings("preview")
@Service
public class ProductRecommendationService {
  private final CustomerService customerService;
  private final OrderService orderService;
  private final PreferenceService preferenceService;

  public ProductRecommendationService(CustomerService customerService, OrderService orderService, PreferenceService preferenceService) {
    this.customerService = customerService;
    this.orderService = orderService;
    this.preferenceService = preferenceService;
  }

  public List<Product> recommendProducts(String customerId) {
    System.out.println(Thread.currentThread());
    List<Order> orders = orderService.fetchOrderHistory(customerId);
    Optional<Customer> customer = customerService.fetchCustomer(customerId);
    List<CustomerPreference> customerPreference = preferenceService.fetchCustomerPreferences(customerId);

    return customer
      .map(it -> prepareProductRecommendation(it, customerPreference, orders))
      .orElse(Collections.emptyList());
  }

  public CompletableFuture<List<Product>> recommendProductsAsync(String customerId) {
    CompletableFuture<List<Order>> orders = orderService.fetchOrderHistoryAsync(customerId);
    CompletableFuture<Optional<Customer>> customer = customerService.fetchCustomerAsync(customerId);
    CompletableFuture<List<CustomerPreference>> customerPreference = preferenceService.fetchCustomerPreferencesAsync(customerId);

    return CompletableFuture.allOf(orders, customerPreference, customer)
      .thenApplyAsync(res ->
        toRecommendedProduct(customer.join(), customerPreference.join(), orders.join())
      );
  }

  private List<Product> toRecommendedProduct(Optional<Customer> customer, List<CustomerPreference> customerPreference, List<Order> orders) {
    return customer
      .map(it -> prepareProductRecommendation(it, customerPreference, orders))
      .orElse(Collections.emptyList());
  }

  private List<Product> prepareProductRecommendation(
    Customer customer,
    List<CustomerPreference> preferences,
    List<Order> orders) {
    return List.of(
      new Product(UUID.randomUUID(), "AirPods", BigDecimal.valueOf(12000))
    );
  }

  public List<Product> recommendProductsStructured(String customerId) throws InterruptedException {
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
      var orderHistory = scope.fork(() -> orderService.fetchOrderHistory(customerId));
      var customer = scope.fork(() -> customerService.fetchCustomer(customerId));
      var customerPref = scope.fork(() -> preferenceService.fetchCustomerPreferences(customerId));

      scope.join().throwIfFailed(RuntimeException::new);
      return customer.get()
        .map(it -> prepareProductRecommendation(it, customerPref.get(), orderHistory.get()))
        .orElse(Collections.emptyList());
    }
  }
}
