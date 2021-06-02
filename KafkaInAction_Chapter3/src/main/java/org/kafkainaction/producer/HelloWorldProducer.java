package org.kafkainaction.producer;

import org.kafkainaction.Alert;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Properties;

import static org.kafkainaction.alert_status.Critical;

public class HelloWorldProducer {

  static final Logger log = LoggerFactory.getLogger(HelloWorldProducer.class);

  public static void main(String[] args) {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("key.serializer", "org.apache.kafka.common.serialization.LongSerializer");
    props.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
    props.put("schema.registry.url", "http://localhost:8081");

    try (Producer<Long, Alert> producer = new KafkaProducer<>(props)) {
      Alert alert = new Alert(12345L, Instant.now().toEpochMilli(), Critical);

      log.info("Alert -> {}", alert);

      ProducerRecord<Long, Alert> producerRecord =
          new ProducerRecord<>("avrotest",
                               alert.getSensorId(),
                               alert);

      producer.send(producerRecord);
    }
  }

}
