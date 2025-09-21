package dev.ctirouzh.debezium.elastic.product

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.ctirouzh.debezium.repository.Product
import dev.ctirouzh.debezium.services.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class ProductCdcConsumer(
  @param:Autowired private val productDocumentRepository: ProductDocumentRepository,
  @param:Autowired private val productService: ProductService // Use the service from the previous step
) {
  private val objectMapper: ObjectMapper = jacksonObjectMapper()

  @KafkaListener(
    topics = [
      "cdc.debezium.products",
      "cdc.debezium.prices",
      "cdc.debezium.media",
    ],
    containerFactory = "batchListenerContainerFactory",
  )
  fun handleDebeziumEvent(events: List<String>) {
    val productIdsToUpdate = mutableSetOf<Long>()
    for (event in events) {
      println(event)
      val payload: JsonNode = objectMapper.readTree(event)
      val sourceNode = payload.get("source")
      val table = sourceNode.get("table").asText()
      if (table !in listOf("products", "prices", "media")) {
        println("Skipping unknown table: $table")
        continue
      }
      val op = payload.get("op")?.asText()?: continue
      if (op !in listOf("c", "u", "d")) {
        println("Skipping unknown operation: $op")
        continue
      }
      val id = when (table) {
        "products" -> payload.getOpNodeId(op, "id")
        "prices"   -> payload.getOpNodeId(op, "product_id")
        "media"    -> payload.getMediaProductIdIfApplicable(op)
        else       -> null
      }
      id?.let(productIdsToUpdate::add)
    }

    // Use the new service method to fetch all products and their media in a single batch
    val products = productService.getProductsWithMediaAndPrices(productIdsToUpdate)

    val documentsToSave = products.map { convertToProductDocument(it) }

    if (documentsToSave.isNotEmpty()) {
      productDocumentRepository.saveAll(documentsToSave)
    }

    // Handle deletions for any products that were deleted
    val deletedProductIds = productIdsToUpdate.filter { id -> products.none { it.id == id } }
    // Perform a single batch delete to Elasticsearch
    if (deletedProductIds.isNotEmpty()) {
      productDocumentRepository.deleteAllById(deletedProductIds.map { it.toString() })
    }
  }

  private fun convertToProductDocument(product: Product): ProductDocument {
    val prices = product.prices.map {
      ProductDocument.PriceDocument(
        id = it.id?.toString() ?: "",
        amount = it.amount,
        currency = it.currency.toString(),
        priceType = it.priceType.toString(),
        validFrom = it.validFrom,
        validTo = it.validTo,
      )
    }

    val media = product.media.map {
      ProductDocument.MediaDocument(
        id = it.id?.toString() ?: "",
        url = it.url,
        mediaType = it.mediaType.toString(),
        altText = it.altText,
        sortOrder = it.sortOrder,
      )
    }

    return ProductDocument(
      id = product.id?.toString() ?: "",
      name = product.name,
      description = product.description,
      prices = prices,
      media = media,
      status = product.status.toString(),
      createdAt = product.createdAt,
      updatedAt = product.updatedAt,
    )
  }
}

private fun JsonNode.getOpNodeId(op: String, field: String): Long? {
  val node = if (op == "d") this.get("before") else this.get("after")
  return node?.get(field)?.takeIf { it.isIntegralNumber }?.asLong()
}

private fun JsonNode.getMediaProductIdIfApplicable(op: String): Long? {
  val node = if (op == "d") this.get("before") else this.get("after")
  return node
    ?.takeIf { it.get("entity")?.asText() == "products" }
    ?.get("entity_id")
    ?.takeIf { it.isIntegralNumber }
    ?.asLong()
}