package io.github.cafeteru.gft.prices.infrastructure.adapter.in.api;

import static java.util.Objects.isNull;

import io.github.cafeteru.gft.adapters.api.PricesApi;
import io.github.cafeteru.gft.common.util.DateConverter;
import io.github.cafeteru.gft.domain.model.PriceRS;
import io.github.cafeteru.gft.prices.application.port.in.PricePort;
import java.time.LocalDateTime;
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
    if (isNull(idProduct) || idProduct <= 0 || isNull(idBrand) || idBrand <= 0) {
      throw new IllegalArgumentException("Invalid product or brand");
    }
    final LocalDateTime localDate = dateConverter.stringToLocalDateTime(applicationDate);
    final PriceRS priceRS = pricePort.getPrice(localDate, idProduct, idBrand);
    return Objects.nonNull(priceRS) ?
        ResponseEntity.ok(priceRS) :
        ResponseEntity.notFound().build();
  }
}
