package dev.ctirouzh.debezium.entity

import jakarta.persistence.*

@Entity
@Table(name = "media")
data class Media(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,

  var url: String = "",

  var entity: String = "",

  @Column(name = "entity_id")
  var entityId: Long? = null
)
