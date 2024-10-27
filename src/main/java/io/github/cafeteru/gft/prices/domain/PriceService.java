package io.github.cafeteru.gft.prices.domain;

import io.github.cafeteru.gft.domain.model.PriceRS;
import io.github.cafeteru.gft.prices.adapter.api.mapper.PriceMapper;
import io.github.cafeteru.gft.prices.adapter.db.PriceRepository;
import io.github.cafeteru.gft.prices.adapter.db.model.Price;
import io.github.cafeteru.gft.prices.port.PricePort;
import java.time.LocalDateTime;
import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceService implements PricePort {

  private final PriceRepository repository;
  private final PriceMapper priceMapper;

  @Override
  public PriceRS getPrice(final LocalDateTime applicationDate,
      final Integer productId, final Integer brandId) {
    final var priceList = repository.getPrice(applicationDate, productId, brandId);
    final var result = priceList.isEmpty() ? null :
        priceList.stream().max(Comparator.comparing(Price::getPriority))
            .orElse(priceList.getFirst());
    return priceMapper.toPriceRS(result);
  }
}
