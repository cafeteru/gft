package io.github.cafeteru.gft.prices.adapter.db;

import io.github.cafeteru.gft.prices.adapter.db.model.Price;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PriceRepository extends JpaRepository<Price, Long> {

  @Query(name = "Prices.getPrice")
  List<Price> getPrice(
      @Param("dateTime") LocalDateTime dateTime,
      @Param("productId") Integer productId,
      @Param("brandId") Integer brandId);
}
