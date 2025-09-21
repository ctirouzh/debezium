package dev.ctirouzh.debezium.services

// ProductService.kt
import dev.ctirouzh.debezium.repository.Media
import dev.ctirouzh.debezium.repository.Product
import dev.ctirouzh.debezium.repository.MediaRepository
import dev.ctirouzh.debezium.repository.ProductRepository
import org.hibernate.Hibernate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
  private val productRepository: ProductRepository,
  private val mediaRepository: MediaRepository
) {

  @Transactional(readOnly = true)
  fun getProduct(productId: Long): Product? {
    return productRepository.findById(productId).orElse(null)
  }

  @Transactional(readOnly = true)
  fun getProductsWithMediaAndPrices(productIds: Set<Long>): List<Product> {
    val products = productRepository.findAllWithPricesById(productIds)

    // Use a set of IDs to query for all media in a single batch
    val mediaList = mediaRepository.findAllByEntityAndEntityIdIn("products", productIds)

    // Group the media by entityId (which is the productId in this case)
    val mediaByProductId = mediaList.groupBy { it.entityId }

    // Set the media list on each product
    products.forEach { product ->
      product.media = mediaByProductId[product.id]?.toMutableList() ?: mutableListOf()
      println("Product ${product.id} has prices=${product.prices.size}, media=${product.media.size}")
    }

    return products
  }
}