package com.kafkainaction.producer;

import com.kafkainaction.model.Alert;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class HealthTrendingProducer {

  private static final Logger log = LoggerFactory.getLogger(HealthTrendingProducer.class);

  public static void main(String[] args) throws InterruptedException, ExecutionException {

    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092,localhost:9093");
    props.put("key.serializer", "com.kafkainaction.serde.AlertKeySerde");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    try (Producer<Alert, String> producer = new KafkaProducer<>(props)) {
      Alert alert = new Alert(0, "Stage 0", "CRITICAL", "Stage 0 stopped");
      ProducerRecord<Alert, String> producerRecord = new ProducerRecord<>("healthtrend", alert,
                                                                          alert.getAlertMessage()); // #A <1>

      RecordMetadata result = producer.send(producerRecord).get();
      log.info("offset = {}, topic = {}, timestamp = {}", result.offset(), result.topic(), result.timestamp());
    }
  }
}
