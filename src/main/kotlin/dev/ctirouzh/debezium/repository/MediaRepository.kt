package dev.ctirouzh.debezium.repository

import org.springframework.data.jpa.repository.JpaRepository

interface MediaRepository : JpaRepository<Media, Long> {
  fun findAllByEntityAndEntityIdIn(entity: String, entityIds: Set<Long>): List<Media>
}