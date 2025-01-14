package io.github.cafeteru.gft.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DateConverterTest {

  private final DateConverter dateConverter = new DateConverter();

  @Test
  void when_stringToLocalDateTime_get_correct_date_should_it_works() {
    final String applicationDate = "2021-01-01-01.20.30";
    final LocalDateTime result = dateConverter.stringToLocalDateTime(applicationDate);
    assertEquals(2021, result.getYear());
    assertEquals(1, result.getMonthValue());
    assertEquals(1, result.getDayOfMonth());
    assertEquals(1, result.getHour());
    assertEquals(20, result.getMinute());
    assertEquals(30, result.getSecond());
  }

  @Test
  void when_stringToLocalDateTime_get_a_null_date_should_return_an_exception() {
    try {
      dateConverter.stringToLocalDateTime(null);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid LocalDateTime: " + null, e.getMessage());
    }
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "",
      "2020",
      "2020-06",
      "2020-06-14",
      "2020-06-15-10",
      "2020-06-15-10.00",
      "2020-06-15 10:00:00",
      "invalid-date",
  })
  void when_stringToLocalDateTime_get_a_invalid_date_should_return_an_exception(
      final String applicationDate) {
    try {
      dateConverter.stringToLocalDateTime(applicationDate);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid LocalDateTime: " + applicationDate, e.getMessage());
    }
  }

  @Test
  void when_localDateTimeToString_get_correct_date_should_works() {
    final LocalDateTime localDateTime = LocalDateTime.of(2021, 1, 1, 1, 20, 30);
    final String result = dateConverter.localDateTimeToString(localDateTime);
    assertEquals("2021-01-01-01.20.30", result);
  }

  @Test
  void when_localDateTimeToString_get_null_date_should_return_an_exception() {
    try {
      dateConverter.localDateTimeToString(null);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid LocalDateTime: " + null, e.getMessage());
    }
  }
}
