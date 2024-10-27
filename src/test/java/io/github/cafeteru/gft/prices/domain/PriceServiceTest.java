package io.github.cafeteru.gft.prices.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import io.github.cafeteru.gft.domain.model.PriceRS;
import io.github.cafeteru.gft.prices.adapter.api.mapper.PriceMapper;
import io.github.cafeteru.gft.prices.adapter.db.PriceRepository;
import io.github.cafeteru.gft.prices.adapter.db.model.Price;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PriceServiceTest {

  private PriceService priceService;

  @Mock
  private PriceRepository priceRepository;

  @Mock
  private PriceMapper priceMapper;

  private final LocalDateTime dateTime = LocalDateTime.now();
  private final Integer productId = 1;
  private final Integer brandId = 1;

  private Price price;

  @BeforeEach
  void setUp() {
    price = Price.builder()
        .productId(productId)
        .brandId(brandId)
        .priceList(1)
        .startDate(dateTime)
        .endDate(dateTime)
        .price(BigDecimal.ONE)
        .priority(0)
        .build();
    priceService = new PriceService(priceRepository, priceMapper);
  }

  @Test
  void when_getPrice_not_found_results_should_return_null() {
    when(priceRepository.getPrice(eq(dateTime), eq(productId), eq(brandId))).thenReturn(
        Collections.emptyList());

    final var result = priceService.getPrice(dateTime, productId, brandId);

    assertNull(result);
  }

  @Test
  void when_getPrice_found_one_result_should_return_it() {
    when(priceRepository.getPrice(eq(dateTime), eq(productId), eq(brandId))).thenReturn(
        Collections.singletonList(price));
    when(priceMapper.toPriceRS(any())).thenReturn(new PriceRS());
    final var expected = new PriceRS();

    final var result = priceService.getPrice(dateTime, productId, brandId);

    assertNotNull(result);
    assertEquals(expected.getBrandId(), result.getBrandId());
  }

  @Test
  void when_getPrice_found_many_result_should_return_the_one_with_the_highest_priority() {
    when(priceMapper.toPriceRS(any())).thenReturn(new PriceRS());
    final var pricePriority = Price.builder()
        .productId(10)
        .brandId(10)
        .priceList(10)
        .startDate(LocalDateTime.MAX)
        .endDate(LocalDateTime.MAX)
        .price(BigDecimal.TEN)
        .priority(1)
        .build();
    final var priceRs = new PriceRS();
    priceRs.setBrandId(10);
    when(priceMapper.toPriceRS(pricePriority)).thenReturn(priceRs);
    when(priceRepository.getPrice(eq(dateTime), eq(productId), eq(brandId))).thenReturn(
        List.of(price, pricePriority));

    final var expected = priceMapper.toPriceRS(pricePriority);

    final var result = priceService.getPrice(dateTime, productId, brandId);
    assertNotNull(result);
    assertEquals(expected, result);
  }
}