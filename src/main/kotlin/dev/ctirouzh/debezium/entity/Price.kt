package dev.ctirouzh.debezium.entity

import jakarta.persistence.*


@Entity
@Table(name = "prices")
data class Price(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,

  var amount: Double = 0.0,

  var unit: String = "",

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  var product: Product? = null
)