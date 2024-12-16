package io.github.cafeteru.gft.prices.adapter.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import io.github.cafeteru.gft.common.dates.DateConverter;
import io.github.cafeteru.gft.domain.model.PriceRS;
import io.github.cafeteru.gft.prices.application.service.PriceService;
import io.github.cafeteru.gft.prices.infrastructure.adapter.in.api.PriceController;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class PriceControllerTest {

  @Mock
  private PriceService priceService;

  private final DateConverter dateConverter = new DateConverter();
  private final String applicationDate = "2020-06-13-23.59.00";
  private final int productId = 35455;
  private final int brandId = 1;

  private LocalDateTime localDateTime;
  private PriceController priceController;

  @BeforeEach
  void setUp() {
    priceController = new PriceController(priceService, dateConverter);
    localDateTime = dateConverter.stringToLocalDateTime(applicationDate);
  }

  @Test
  void when_getPrice_not_obtain_a_price_should_return_empty() {
    when(priceService.getPrice(eq(localDateTime), eq(productId), eq(brandId))).thenReturn(null);

    final var result = priceController.getPrice(applicationDate, productId, brandId);

    assertEquals(HttpStatus.NO_CONTENT.value(), result.getStatusCode().value());
  }

  @Test
  void when_getPrice_obtain_a_price_should_return_ok() {
    final var priceRS = new PriceRS();
    when(priceService.getPrice(eq(localDateTime), eq(productId), eq(brandId))).thenReturn(priceRS);

    final var result = priceController.getPrice(applicationDate, productId, brandId);

    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
    assertEquals(priceRS, result.getBody());
  }
}
