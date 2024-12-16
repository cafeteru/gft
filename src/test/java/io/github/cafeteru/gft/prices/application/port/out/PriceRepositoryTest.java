package io.github.cafeteru.gft.prices.application.port.out;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.cafeteru.gft.common.config.TestContainersConfig;
import io.github.cafeteru.gft.common.util.DateConverter;
import io.github.cafeteru.gft.prices.infrastructure.adapter.out.db.model.PriceEntity;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import({TestContainersConfig.class, DateConverter.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class PriceRepositoryTest {

  @Autowired
  private PriceRepository priceRepository;

  @Autowired
  private DateConverter dateConverter;

  private final int productId = 35455;
  private final int brandId = 1;

  @BeforeEach
  public void init() {
    priceRepository.deleteAll();
    final List<PriceEntity> priceList = List.of(
        createPriceEntity("2020-06-14-00.00.00", "2020-12-31-23.59.59", 1, 0,
            BigDecimal.valueOf(35.50)),
        createPriceEntity("2020-06-14-15.00.00", "2020-06-14-18.30.00", 2, 1,
            BigDecimal.valueOf(25.45)),
        createPriceEntity("2020-06-15-00.00.00", "2020-06-15-11.00.00", 3, 1,
            BigDecimal.valueOf(30.50)),
        createPriceEntity("2020-06-15-16.00.00", "2020-12-31-23.59.59", 4, 1,
            BigDecimal.valueOf(38.95))
    );
    priceRepository.saveAll(priceList);
  }

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

  private PriceEntity createPriceEntity(
      final String startDate, final String endDate, final Integer priceList, final Integer priority,
      final BigDecimal price) {
    final var startDateConverted = dateConverter.stringToLocalDateTime(startDate);
    final var endDateConverted = dateConverter.stringToLocalDateTime(endDate);
    return PriceEntity.builder()
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
