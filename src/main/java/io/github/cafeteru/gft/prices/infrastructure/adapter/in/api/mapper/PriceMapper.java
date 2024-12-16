package io.github.cafeteru.gft.prices.infrastructure.adapter.in.api.mapper;

import io.github.cafeteru.gft.common.dates.DateConverter;
import io.github.cafeteru.gft.domain.model.PriceRS;
import io.github.cafeteru.gft.prices.domain.Price;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {DateConverter.class}
)
public interface PriceMapper {

  @Mapping(target = "finalPrice", source = "price")
  PriceRS toPriceRS(Price price);
}
