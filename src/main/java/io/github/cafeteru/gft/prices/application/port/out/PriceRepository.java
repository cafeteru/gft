package io.github.cafeteru.gft.prices.application.port.out;

import io.github.cafeteru.gft.prices.infrastructure.adapter.out.db.model.PriceEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PriceRepository extends JpaRepository<PriceEntity, Long> {

  @Query(name = "PriceEntity.getPrice")
  List<PriceEntity> getPrice(
      @Param("dateTime") LocalDateTime dateTime,
      @Param("productId") Integer productId,
      @Param("brandId") Integer brandId);
}
