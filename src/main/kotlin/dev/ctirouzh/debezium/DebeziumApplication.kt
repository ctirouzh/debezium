package dev.ctirouzh.debezium

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = ["dev.ctirouzh.debezium.elastic.product"])
class DebeziumApplication

fun main(args: Array<String>) {
  runApplication<DebeziumApplication>(*args)
}
