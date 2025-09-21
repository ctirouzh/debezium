package dev.ctirouzh.debezium.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ProductRepository : JpaRepository<Product, Long> {
  @Query("""
    SELECT DISTINCT p 
    FROM Product p 
    LEFT JOIN FETCH p.prices 
    WHERE p.id IN :ids
""")
  fun findAllWithPricesById(@Param("ids") ids: Set<Long>): List<Product>
}