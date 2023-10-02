package com.example.fibers.services;

import com.example.fibers.models.Customer;
import com.example.fibers.models.CustomerPreference;
import com.example.fibers.models.Order;
import com.example.fibers.models.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
        List<Order> orders = orderService.fetchOrderHistory(customerId);
        List<CustomerPreference> customerPreference = preferenceService.fetchCustomerPreferences(customerId);
        Optional<Customer> customer = customerService.fetchCustomer(customerId);

        return customer
                .map(it -> prepareProductRecommendation(it, customerPreference, orders))
                .orElse(Collections.emptyList());

    }

    public CompletableFuture<List<Product>> recommendProductsAsync(String customerId) throws InterruptedException {
        CompletableFuture<List<Order>> orders = getOrderAsync(customerId);
        CompletableFuture<Optional<Customer>> customer = getCustomerAsync(customerId);
        CompletableFuture<List<CustomerPreference>> customerPreference = getCustomerPreferenceAsync(customerId);

        CompletableFuture<Void> all = CompletableFuture.allOf(orders, customerPreference, customer);

        return all.thenApply(res -> customer.join()
                .map(it -> prepareProductRecommendation(it, customerPreference.join(), orders.join()))
                .orElse(Collections.emptyList()));
    }

    private CompletableFuture<List<CustomerPreference>> getCustomerPreferenceAsync(String customerId) {
        return CompletableFuture.supplyAsync(() -> preferenceService.fetchCustomerPreferences(customerId));
    }

    private CompletableFuture<List<Order>> getOrderAsync(String customerId) {
        return CompletableFuture.supplyAsync(() -> orderService.fetchOrderHistory(customerId));
    }

    private CompletableFuture<Optional<Customer>> getCustomerAsync(String customerId) throws InterruptedException {
        return CompletableFuture.supplyAsync(() -> customerService.fetchCustomer(customerId));
    }

    private List<Product> prepareProductRecommendation(
            Customer customer,
            List<CustomerPreference> preferences,
            List<Order> orders) {
        return List.of(
                new Product(UUID.randomUUID(), "AirPods", BigDecimal.valueOf(12000))
        );
    }
}
