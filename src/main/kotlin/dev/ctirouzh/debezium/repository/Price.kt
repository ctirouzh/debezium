package dev.ctirouzh.debezium.repository

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime


@Entity
@Table(name = "prices")
data class Price(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,

  var amount: Double = 0.0,

  @Enumerated(EnumType.STRING)
  var currency: Currency = Currency.USD,

  @Enumerated(EnumType.STRING)
  var priceType: PriceType = PriceType.RETAIL,

  @Column(name = "valid_from")
  var validFrom: LocalDateTime? = null,

  @Column(name = "valid_to")
  var validTo: LocalDateTime? = null,

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  var product: Product? = null
)

enum class Currency {
  USD, EUR, GBP, JPY
}

enum class PriceType {
  RETAIL, WHOLESALE, DISCOUNT
}
