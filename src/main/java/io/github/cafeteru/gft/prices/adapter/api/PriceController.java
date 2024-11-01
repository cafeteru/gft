package io.github.cafeteru.gft.prices.adapter.api;

import io.github.cafeteru.gft.adapters.api.PricesApi;
import io.github.cafeteru.gft.common.dates.DateConverter;
import io.github.cafeteru.gft.domain.model.PriceRS;
import io.github.cafeteru.gft.prices.port.PricePort;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PriceController implements PricesApi {

  private final PricePort pricePort;
  private final DateConverter dateConverter;

  @Override
  public ResponseEntity<PriceRS> getPrice(final String applicationDate, final Integer idProduct,
      final Integer idBrand) {
    final var localDate = dateConverter.stringToLocalDateTime(applicationDate);
    final var priceRS = pricePort.getPrice(localDate, idProduct, idBrand);
    return Objects.nonNull(priceRS) ?
        ResponseEntity.ok(priceRS) :
        ResponseEntity.noContent().build();
  }
}
