package io.github.cafeteru.gft.prices.infrastructure.adapter.in.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import io.github.cafeteru.gft.common.util.DateConverter;
import io.github.cafeteru.gft.domain.model.PriceRS;
import io.github.cafeteru.gft.prices.application.service.PriceService;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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

    assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatusCode().value());
  }

  @Test
  void when_getPrice_obtain_a_price_should_return_ok() {
    final var priceRS = new PriceRS();
    when(priceService.getPrice(eq(localDateTime), eq(productId), eq(brandId))).thenReturn(priceRS);

    final var result = priceController.getPrice(applicationDate, productId, brandId);

    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
    assertEquals(priceRS, result.getBody());
  }

  @ParameterizedTest
  @MethodSource("invalidInputs")
  void when_getPrice_with_invalid_inputs_should_throw_exception(
      final Integer idProduct, final Integer idBrand) {
    assertThrows(IllegalArgumentException.class, () ->
        priceController.getPrice("2023-01-01T00:00:00", idProduct, idBrand));
  }

  static Stream<Arguments> invalidInputs() {
    return Stream.of(
        Arguments.of(null, 1),
        Arguments.of(1, null),
        Arguments.of(-1, 1),
        Arguments.of(1, -1),
        Arguments.of(0, 1),
        Arguments.of(1, 0)
    );
  }


}
