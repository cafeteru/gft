package io.github.cafeteru.gft.prices.application.service;

import io.github.cafeteru.gft.domain.model.PriceRS;
import io.github.cafeteru.gft.prices.application.port.in.PricePort;
import io.github.cafeteru.gft.prices.application.port.out.PriceRepository;
import io.github.cafeteru.gft.prices.domain.Price;
import io.github.cafeteru.gft.prices.infrastructure.adapter.in.api.mapper.PriceMapper;
import io.github.cafeteru.gft.prices.infrastructure.adapter.out.db.mapper.PriceEntityMapper;
import io.github.cafeteru.gft.prices.infrastructure.adapter.out.db.model.PriceEntity;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceService implements PricePort {

  private final PriceRepository repository;
  private final PriceEntityMapper priceEntityMapper;
  private final PriceMapper priceMapper;

  @Override
  public PriceRS getPrice(final LocalDateTime applicationDate,
      final Integer productId, final Integer brandId) {
    final List<PriceEntity> priceEntities = repository.getPrice(applicationDate, productId,
        brandId);
    final List<Price> prices = priceEntities.stream().map(priceEntityMapper::toPrice).toList();
    final Price result = prices.isEmpty() ? null :
        prices.stream().max(Comparator.comparing(Price::getPriority)).orElse(prices.getFirst());
    return priceMapper.toPriceRS(result);
  }
}
