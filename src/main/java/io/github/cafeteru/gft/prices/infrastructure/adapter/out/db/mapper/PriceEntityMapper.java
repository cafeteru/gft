package io.github.cafeteru.gft.prices.infrastructure.adapter.out.db.mapper;

import io.github.cafeteru.gft.prices.domain.Price;
import io.github.cafeteru.gft.prices.infrastructure.adapter.out.db.model.PriceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PriceEntityMapper {

  Price toDomain(PriceEntity entity);

  PriceEntity toEntity(Price price);

  @Mapping(target = "id", ignore = true)
  void updateEntityFromDomain(Price price, @MappingTarget PriceEntity entity);
}
