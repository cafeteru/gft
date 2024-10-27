package io.github.cafeteru.gft.prices.port;

import io.github.cafeteru.gft.domain.model.PriceRS;
import java.time.LocalDateTime;


public interface PricePort {

  PriceRS getPrice(final LocalDateTime applicationDate, final Integer productId,
      final Integer brandId);
}
