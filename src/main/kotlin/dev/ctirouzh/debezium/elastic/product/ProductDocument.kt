package dev.ctirouzh.debezium.elastic.product
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.*
import java.time.LocalDateTime

@Document(indexName = "products")
@Setting(shards = 1, replicas = 0)
data class ProductDocument(

  @Id
  val id: String,

  @Field(type = FieldType.Text)
  val name: String,

  @Field(type = FieldType.Text)
  val description: String,

  @Field(type = FieldType.Nested)
  val prices: List<PriceDocument>,

  @Field(type = FieldType.Nested)
  val media: List<MediaDocument>,

  @Field(type = FieldType.Keyword)
  val status: String,  // ACTIVE, INACTIVE, etc.

  @Field(type = FieldType.Date)
  val createdAt: LocalDateTime,

  @Field(type = FieldType.Date)
  val updatedAt: LocalDateTime?
) {

  data class PriceDocument(
    @Field(type = FieldType.Keyword)
    val id: String,

    @Field(type = FieldType.Double)
    val amount: Double,

    @Field(type = FieldType.Keyword)
    val currency: String,

    @Field(type = FieldType.Keyword)
    val priceType: String, // RETAIL, WHOLESALE, etc.

    @Field(type = FieldType.Date)
    val validFrom: LocalDateTime? = null,

    @Field(type = FieldType.Date)
    val validTo: LocalDateTime? = null
  )

  data class MediaDocument(
    @Field(type = FieldType.Keyword)
    val id: String,

    @Field(type = FieldType.Keyword)
    val url: String,

    @Field(type = FieldType.Keyword)
    val mediaType: String,

    @Field(type = FieldType.Text)
    val altText: String? = null,

    @Field(type = FieldType.Integer)
    val sortOrder: Int? = null
  )
}
