package com.example.concurrency.services;

import com.example.concurrency.models.Customer;
import com.example.concurrency.models.CustomerPreference;
import com.example.concurrency.models.Order;
import com.example.concurrency.models.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("preview")
@Service
public class ProductRecommendationService {
  private final CustomerService customerService;
  private final OrderService orderService;
  private final PreferenceService preferenceService;

  public ProductRecommendationService(
    CustomerService customerService,
    OrderService orderService,
    PreferenceService preferenceService
  ) {
    this.customerService = customerService;
    this.orderService = orderService;
    this.preferenceService = preferenceService;
  }

  public List<Product> recommendProducts(String customerId) {
    List<Order> orders = orderService.fetchOrderHistory(customerId);
    Optional<Customer> customer = customerService.fetchCustomer(customerId);
    List<CustomerPreference> customerPreference = preferenceService.fetchCustomerPreferences(customerId);

    return customer
      .map(it -> prepareProductRecommendation(it, customerPreference, orders))
      .orElse(Collections.emptyList());
  }

  public List<Product> recommendProductsT1(String customerId) throws InterruptedException {
    AtomicReference<List<Order>> ordersRef = new AtomicReference<>();
    Thread thread1 = new Thread(() -> {
      List<Order> orders = orderService.fetchOrderHistory(customerId);
      ordersRef.set(orders);
    });
    AtomicReference<Optional<Customer>> custRef = new AtomicReference<>();
    Thread thread2 = new Thread(() -> {
      Optional<Customer> customer = customerService.fetchCustomer(customerId);
      custRef.set(customer);
    });
    AtomicReference<List<CustomerPreference>> custPrefRef = new AtomicReference<>();
    Thread thread3 = new Thread(() -> {
      List<CustomerPreference> customerPreference = preferenceService.fetchCustomerPreferences(customerId);
      custPrefRef.set(customerPreference);
    });
    thread1.start();
    thread2.start();
    thread3.start();
    thread1.join();
    thread2.join();
    thread3.join();
    return custRef.get()
      .map(it -> prepareProductRecommendation(it, custPrefRef.get(), ordersRef.get()))
      .orElse(Collections.emptyList());
  }

  public List<Product> recommendProductsThreadPool(String customerId) throws ExecutionException, InterruptedException {
    try (ExecutorService service = Executors.newFixedThreadPool(10)) {
      var ordersFuture = service.submit(() -> orderService.fetchOrderHistory(customerId));
      var customerFuture = service.submit(() -> customerService.fetchCustomer(customerId));
      var custPrefFuture = service.submit(() -> preferenceService.fetchCustomerPreferences(customerId));

      List<CustomerPreference> preferences = custPrefFuture.get();
      List<Order> orders = ordersFuture.get();
      return customerFuture.get()
        .map(it -> prepareProductRecommendation(it, preferences, orders))
        .orElse(Collections.emptyList());
    }
  }

  public List<Product> recommendProductsForkJoinPool(String customerId) throws ExecutionException, InterruptedException {
    try (ExecutorService service = ForkJoinPool.commonPool()) {
      var ordersFuture = service.submit(() -> orderService.fetchOrderHistory(customerId));
      var customerFuture = service.submit(() -> customerService.fetchCustomer(customerId));
      var custPrefFuture = service.submit(() -> preferenceService.fetchCustomerPreferences(customerId));

      List<CustomerPreference> preferences = custPrefFuture.get();
      List<Order> orders = ordersFuture.get();
      return customerFuture.get()
        .map(it -> prepareProductRecommendation(it, preferences, orders))
        .orElse(Collections.emptyList());
    }
  }


  public List<Product> recommendProductsThreadsCancel(String customerId) {
    Future<List<Order>> ordersFuture = null;
    Future<Optional<Customer>> customerFuture = null;
    Future<List<CustomerPreference>> custPrefFuture = null;
    try (ExecutorService service = Executors.newVirtualThreadPerTaskExecutor()) {
      ordersFuture = service.submit(() -> orderService.fetchOrderHistory(customerId));
      customerFuture = service.submit(() -> customerService.fetchCustomer(customerId));
      custPrefFuture = service.submit(() -> preferenceService.fetchCustomerPreferences(customerId));

      List<CustomerPreference> preferences = custPrefFuture.get();
      List<Order> orders = ordersFuture.get();
      return customerFuture.get()
        .map(it -> prepareProductRecommendation(it, preferences, orders))
        .orElse(Collections.emptyList());
    } catch (InterruptedException | ExecutionException e) {
      ordersFuture.cancel(true);
      customerFuture.cancel(true);
      custPrefFuture.cancel(true);

      throw new RuntimeException(e);
    }
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

  public CompletableFuture<List<Product>> recommendProductsAsync2(String customerId) {
    CompletableFuture<List<Order>> orders = orderService.fetchOrderHistoryAsync(customerId);
    CompletableFuture<Optional<Customer>> customer = customerService.fetchCustomerAsync(customerId);
    CompletableFuture<List<CustomerPreference>> customerPreference = preferenceService.fetchCustomerPreferencesAsync(customerId);

    return CompletableFuture.allOf(orders, customerPreference, customer)
      .exceptionally(e -> {
        orders.cancel(true);
        customer.cancel(true);
        customerPreference.cancel(true);
        throw new RuntimeException(e);
      })
      .thenApplyAsync(_ ->
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
