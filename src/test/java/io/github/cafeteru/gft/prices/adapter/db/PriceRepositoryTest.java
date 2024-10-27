package io.github.cafeteru.gft.prices.adapter.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.cafeteru.gft.common.dates.DateConverter;
import io.github.cafeteru.gft.config.TestContainersTestConfig;
import io.github.cafeteru.gft.prices.adapter.db.model.Price;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import({TestContainersTestConfig.class, DateConverter.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class PriceRepositoryTest {

  @Autowired
  private PriceRepository priceRepository;

  @Autowired
  private DateConverter dateConverter;

  private final int productId = 35455;
  private final int brandId = 1;

  @Test
  void when_getPrice_with_date_before_all_should_return_a_empty_list() {
    final var dateTime = dateConverter.stringToLocalDateTime("2020-06-13-23.59.00");
    final var found = priceRepository.getPrice(dateTime, productId, brandId);
    assertTrue(found.isEmpty());
  }

  @Test
  void when_getPrice_with_date_after_all_should_return_a_empty_list() {
    final var localDateTime = dateConverter.stringToLocalDateTime("2026-06-13-23.59.00");
    final var found = priceRepository.getPrice(localDateTime, productId, brandId);
    assertTrue(found.isEmpty());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "2020-06-14-14.59.59",
      "2020-06-14-18.30.01",
      "2020-06-14-23.59.59",
      "2020-06-15-11.00.01",
      "2020-06-15-15.59.59"
  })
  void when_getPrice_with_date_without_conflicts_should_return_one_result(String applicationDate) {
    final var localDateTime = dateConverter.stringToLocalDateTime(applicationDate);
    final var found = priceRepository.getPrice(localDateTime, productId, brandId);

    assertEquals(1, found.size());
    assertEquals(BigDecimal.valueOf(35.50).setScale(2, RoundingMode.HALF_UP),
        found.getFirst().getPrice().setScale(2, RoundingMode.HALF_UP));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "2020-06-14-15.00.00",
      "2020-06-14-18.30.00",
      "2020-06-15-00.00.00",
      "2020-06-15-11.00.00",
      "2020-06-15-16.00.00"
  })
  void when_getPrice_with_date_with_conflicts_should_return_many_result(String applicationDate) {
    final var localDateTime = dateConverter.stringToLocalDateTime(applicationDate);
    final var found = priceRepository.getPrice(localDateTime, productId, brandId);
    assertFalse(found.isEmpty());
    assertNotEquals(1, found.size());
  }

  private Price createPrice(
      final String startDate, final String endDate, final Integer priceList, final Integer priority,
      final BigDecimal price) {
    final var startDateConverted = dateConverter.stringToLocalDateTime(startDate);
    final var endDateConverted = dateConverter.stringToLocalDateTime(endDate);
    return Price.builder()
        .brandId(brandId)
        .startDate(startDateConverted)
        .endDate(endDateConverted)
        .priceList(priceList)
        .productId(productId)
        .priority(priority)
        .price(price)
        .curr("EUR")
        .build();
  }
}
