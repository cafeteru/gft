package io.github.cafeteru.gft.prices.adapter.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import io.github.cafeteru.gft.common.dates.DateConverter;
import io.github.cafeteru.gft.domain.model.PriceRS;
import io.github.cafeteru.gft.prices.adapter.db.model.Price;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PriceMapperTest {

  @InjectMocks
  private PriceMapperImpl priceMapper;

  @Mock
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

    when(dateConverter.localDateTimeToString(price.getStartDate())).thenReturn("2023-01-01T00:00:00");
    when(dateConverter.localDateTimeToString(price.getEndDate())).thenReturn("2023-12-31T23:59:59");
  }

  @Test
  void testToPriceRS() {
    final PriceRS priceRS = priceMapper.toPriceRS(price);

    assertEquals(price.getProductId(), priceRS.getProductId());
    assertEquals(price.getBrandId(), priceRS.getBrandId());
    assertEquals(price.getPriceList(), priceRS.getPriceList());
    assertEquals("2023-01-01T00:00:00", priceRS.getStartDate());
    assertEquals("2023-12-31T23:59:59", priceRS.getEndDate());
    assertEquals(price.getPrice().doubleValue(), priceRS.getFinalPrice());
  }

  @Test
  void testToPriceRS_WhenPriceIsNull_ShouldMapFinalPriceAsNull() {
    price.setPrice(null);

    final PriceRS priceRS = priceMapper.toPriceRS(price);

    assertEquals(price.getProductId(), priceRS.getProductId());
    assertEquals(price.getBrandId(), priceRS.getBrandId());
    assertEquals(price.getPriceList(), priceRS.getPriceList());
    assertEquals("2023-01-01T00:00:00", priceRS.getStartDate());
    assertEquals("2023-12-31T23:59:59", priceRS.getEndDate());
    assertNull(priceRS.getFinalPrice(), "Expected finalPrice to be null when price is null");
  }
}
