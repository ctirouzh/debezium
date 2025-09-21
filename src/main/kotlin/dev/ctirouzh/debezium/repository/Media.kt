package dev.ctirouzh.debezium.repository

import jakarta.persistence.*

@Entity
@Table(name = "media")
data class Media(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,

  var url: String = "",

  @Enumerated(EnumType.STRING)
  var mediaType: MediaType = MediaType.IMAGE,

  var altText: String? = null,

  var sortOrder: Int? = null,

  var entity: String = "",

  @Column(name = "entity_id")
  var entityId: Long? = null
)

enum class MediaType {
  IMAGE, VIDEO, DOCUMENT
}
