package io.github.cafeteru.gft.common.dates;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DateConverter {

  public final String PATTERN = "yyyy-MM-dd-HH.mm.ss";

  public LocalDateTime stringToLocalDateTime(String applicationDate) {
    try {
      final var formatter = DateTimeFormatter.ofPattern(PATTERN);
      return LocalDateTime.parse(applicationDate, formatter);
    } catch (NullPointerException | DateTimeParseException e) {
      log.error("Invalid String value: {}", applicationDate);
      throw new IllegalArgumentException("Invalid LocalDateTime: " + applicationDate);
    }
  }

  public String localDateTimeToString(LocalDateTime localDateTime) {
    try {
      var formatter = DateTimeFormatter.ofPattern(PATTERN);
      return localDateTime.format(formatter);
    } catch (NullPointerException e) {
      log.error("Invalid LocalDateTime value: {}", localDateTime);
      throw new IllegalArgumentException("Invalid LocalDateTime: " + localDateTime);
    }
  }
}
