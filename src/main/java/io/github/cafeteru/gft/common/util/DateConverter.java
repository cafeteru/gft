package io.github.cafeteru.gft.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DateConverter {

  public final String PATTERN = "yyyy-MM-dd-HH.mm.ss";

  public LocalDateTime stringToLocalDateTime(final String applicationDate) {
    try {
      final var formatter = DateTimeFormatter.ofPattern(PATTERN);
      return LocalDateTime.parse(applicationDate, formatter);
    } catch (final NullPointerException | DateTimeParseException e) {
      log.error("Invalid String value: {}", applicationDate);
      throw new IllegalArgumentException("Invalid LocalDateTime: " + applicationDate);
    }
  }

  public String localDateTimeToString(final LocalDateTime localDateTime) {
    try {
      final var formatter = DateTimeFormatter.ofPattern(PATTERN);
      return localDateTime.format(formatter);
    } catch (final NullPointerException e) {
      log.error("Invalid LocalDateTime value: {}", localDateTime);
      throw new IllegalArgumentException("Invalid LocalDateTime: " + localDateTime);
    }
  }
}
