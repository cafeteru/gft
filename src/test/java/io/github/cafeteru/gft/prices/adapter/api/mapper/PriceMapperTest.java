package io.github.cafeteru.gft.prices.adapter.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.github.cafeteru.gft.common.dates.DateConverter;
import io.github.cafeteru.gft.domain.model.PriceRS;
import io.github.cafeteru.gft.prices.adapter.db.model.Price;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PriceMapperTest {

  @Autowired
  private PriceMapper priceMapper;

  @Autowired
  private DateConverter dateConverter;

  private Price price;

  @BeforeEach
  void setUp() {
    price = new Price();
    price.setProductId(35455);
    price.setBrandId(1);
    price.setPriceList(1);
    price.setStartDate(LocalDateTime.of(2023, 1, 1, 0, 0));
    price.setEndDate(LocalDateTime.of(2023, 12, 31, 23, 59));
    price.setPrice(BigDecimal.valueOf(35.50));
  }

  @Test
  void testToPriceRS() {
    final PriceRS priceRS = priceMapper.toPriceRS(price);

    assertEquals(price.getProductId(), priceRS.getProductId());
    assertEquals(price.getBrandId(), priceRS.getBrandId());
    assertEquals(price.getPriceList(), priceRS.getPriceList());
    assertEquals(dateConverter.localDateTimeToString(price.getStartDate()), priceRS.getStartDate());
    assertEquals(dateConverter.localDateTimeToString(price.getEndDate()), priceRS.getEndDate());
    assertEquals(price.getPrice().doubleValue(), priceRS.getFinalPrice());
  }

  @Test
  void testToPriceRS_WhenPriceIsNull_ShouldMapFinalPriceAsNull() {
    price.setPrice(null);

    final PriceRS priceRS = priceMapper.toPriceRS(price);

    assertEquals(price.getProductId(), priceRS.getProductId());
    assertEquals(price.getBrandId(), priceRS.getBrandId());
    assertEquals(price.getPriceList(), priceRS.getPriceList());
    assertEquals(dateConverter.localDateTimeToString(price.getStartDate()), priceRS.getStartDate());
    assertEquals(dateConverter.localDateTimeToString(price.getEndDate()), priceRS.getEndDate());
    assertNull(priceRS.getFinalPrice(), "Expected finalPrice to be null when price is null");
  }
}
