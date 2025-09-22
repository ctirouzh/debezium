package dev.ctirouzh.debezium.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import kotlin.time.Duration

@Configuration
class KafkaConfig {

  @Bean
  fun batchListenerContainerFactory(consumerFactory: ConsumerFactory<String, String>): ConcurrentKafkaListenerContainerFactory<String, String> {
    val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
    factory.consumerFactory = consumerFactory
    factory.isBatchListener = true // This enables batch processing
    factory.containerProperties.pollTimeout = 10000L
    return factory
  }
}