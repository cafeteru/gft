package io.github.cafeteru.gft.prices.infrastructure.adapter.out.db.mapper;

import io.github.cafeteru.gft.prices.domain.Price;
import io.github.cafeteru.gft.prices.infrastructure.adapter.out.db.model.PriceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceEntityMapper {

  Price toPrice(PriceEntity entity);
}
