package dev.ctirouzh.debezium.elastic.product

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface ProductDocumentRepository : ElasticsearchRepository<ProductDocument, String>