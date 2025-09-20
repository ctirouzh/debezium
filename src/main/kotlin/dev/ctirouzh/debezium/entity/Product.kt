package dev.ctirouzh.debezium.entity

import jakarta.persistence.*


@Entity
@Table(name = "products")
data class Product(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,

  var name: String = "",

  var description: String = "",

  @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true)
  var prices: MutableSet<Price> = mutableSetOf(),

  @Transient
  var media: List<Media> = listOf(),

  @Column(name = "default_price_id")
  var defaultPriceId: Long? = null
)