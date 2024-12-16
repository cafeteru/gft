package io.github.cafeteru.gft.prices.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.cafeteru.gft.domain.model.PriceRS;
import io.github.cafeteru.gft.prices.application.port.out.PriceRepository;
import io.github.cafeteru.gft.prices.domain.Price;
import io.github.cafeteru.gft.prices.infrastructure.adapter.in.api.mapper.PriceMapper;
import io.github.cafeteru.gft.prices.infrastructure.adapter.out.db.mapper.PriceEntityMapper;
import io.github.cafeteru.gft.prices.infrastructure.adapter.out.db.model.PriceEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PriceServiceTest {

  @Mock
  private PriceRepository priceRepository;

  @Mock
  private PriceMapper priceMapper;

  @Mock
  private PriceEntityMapper priceEntityMapper;

  @InjectMocks
  private PriceService priceService;

  private final LocalDateTime dateTime = LocalDateTime.now();
  private final Integer productId = 1;
  private final Integer brandId = 1;

  private PriceEntity priceEntity;
  private Price price;
  private PriceRS priceRS;

  @BeforeEach
  void setUp() {
    priceEntity = PriceEntity.builder()
        .productId(productId)
        .brandId(brandId)
        .priceList(1)
        .startDate(dateTime)
        .endDate(dateTime)
        .price(BigDecimal.ONE)
        .priority(0)
        .build();
    price = Price.builder()
        .productId(productId)
        .brandId(brandId)
        .priceList(1)
        .startDate(dateTime)
        .endDate(dateTime)
        .price(BigDecimal.ONE)
        .priority(0)
        .build();
    priceRS = new PriceRS();
    priceRS.setBrandId(brandId);
  }

  @Test
  void when_getPrice_not_found_results_should_return_null() {
    when(priceRepository.getPrice(eq(dateTime), eq(productId), eq(brandId))).thenReturn(
        Collections.emptyList());

    final var result = priceService.getPrice(dateTime, productId, brandId);

    assertNull(result);
    verify(priceRepository).getPrice(dateTime, productId, brandId);
    verify(priceEntityMapper, times(0)).toPrice(any());
    verify(priceMapper, times(1)).toPriceRS(null);
  }

  @Test
  void when_getPrice_found_one_result_should_return_it() {
    when(priceRepository.getPrice(eq(dateTime), eq(productId), eq(brandId))).thenReturn(
        Collections.singletonList(priceEntity));
    when(priceEntityMapper.toPrice(priceEntity)).thenReturn(price);
    when(priceMapper.toPriceRS(price)).thenReturn(priceRS);

    final var result = priceService.getPrice(dateTime, productId, brandId);

    assertNotNull(result);
    assertEquals(priceRS.getBrandId(), result.getBrandId());
    verify(priceEntityMapper, times(1)).toPrice(priceEntity);
    verify(priceMapper, times(1)).toPriceRS(price);
  }

  @Test
  void when_getPrice_found_many_result_should_return_the_one_with_the_highest_priority() {
    final PriceEntity priceEntityPriority = PriceEntity.builder()
        .productId(10)
        .brandId(20)
        .priceList(10)
        .startDate(LocalDateTime.MAX)
        .endDate(LocalDateTime.MAX)
        .price(BigDecimal.TEN)
        .priority(1)
        .build();
    final Price pricePriority = Price.builder()
        .productId(10)
        .brandId(20)
        .priceList(10)
        .startDate(LocalDateTime.MAX)
        .endDate(LocalDateTime.MAX)
        .price(BigDecimal.TEN)
        .priority(1)
        .build();
    priceRS.setBrandId(20);
    when(priceEntityMapper.toPrice(priceEntity)).thenReturn(price);
    when(priceEntityMapper.toPrice(priceEntityPriority)).thenReturn(pricePriority);
    when(priceRepository.getPrice(eq(dateTime), eq(productId), eq(brandId))).thenReturn(
        List.of(priceEntity, priceEntityPriority));
    when(priceMapper.toPriceRS(pricePriority)).thenReturn(priceRS);

    final var result = priceService.getPrice(dateTime, productId, brandId);

    assertNotNull(result);
    assertEquals(priceRS.getBrandId(), result.getBrandId());
    verify(priceEntityMapper, times(1)).toPrice(priceEntity);
    verify(priceMapper, times(0)).toPriceRS(price);
    verify(priceMapper, times(1)).toPriceRS(pricePriority);
  }
}