package io.github.cafeteru.gft.prices.infrastructure.adapter.out.db.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
@Table(name = "PRICES")
public class PriceEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "BRAND_ID")
  private Integer brandId;

  @Column(name = "START_DATE", columnDefinition = "TIMESTAMP")
  private LocalDateTime startDate;

  @Column(name = "END_DATE", columnDefinition = "TIMESTAMP")
  private LocalDateTime endDate;

  @Column(name = "PRICE_LIST")
  private Integer priceList;

  @Column(name = "PRODUCT_ID")
  private Integer productId;

  @Column(name = "PRIORITY")
  private Integer priority;

  @Column(name = "PRICE", precision = 19, scale = 2)
  private BigDecimal price;
  @Column(name = "CURR")

  private String curr;
}
