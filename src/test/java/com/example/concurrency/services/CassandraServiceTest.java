package com.example.concurrency.services;

import com.example.concurrency.repo.CassandraRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CassandraServiceTest {
  private final CassandraRepository cassandraRepository = mock(CassandraRepository.class);
  private final CassandraService cassandraService = new CassandraService(cassandraRepository);

  @Test
  void shouldGetCountForGivenEventType() {
    var customerId = "1";
    when(cassandraRepository.findByCustomerIdAndEventType(customerId, "E1"))
      .thenReturn(3L);
    when(cassandraRepository.findByCustomerIdAndEventType(customerId, "E2"))
      .thenReturn(3L);
    when(cassandraRepository.findByCustomerIdAndEventType(customerId, "E3"))
      .thenReturn(2L);

    Long count = cassandraService.getEventsCount(List.of(customerId)).join();

    assertThat(count).isEqualTo(8L);
  }
}