package dev.ctirouzh.debezium.repository

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "products")
data class Product(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,

  var name: String = "",

  var description: String = "",

  @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
  var prices: MutableList<Price> = mutableListOf(),

  @Transient
  var media: MutableList<Media> = mutableListOf(),

  @Enumerated(EnumType.STRING)
  var status: ProductStatus = ProductStatus.ACTIVE,

  @Column(name = "created_at", nullable = false, updatable = false)
  var createdAt: LocalDateTime = LocalDateTime.now(),

  @Column(name = "updated_at")
  var updatedAt: LocalDateTime? = null
)

enum class ProductStatus {
  ACTIVE, INACTIVE, DISCONTINUED
}
