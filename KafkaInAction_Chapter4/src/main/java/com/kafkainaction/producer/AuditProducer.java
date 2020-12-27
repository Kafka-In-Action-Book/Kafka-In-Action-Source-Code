package com.kafkainaction.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class AuditProducer {

  private static final Logger log = LoggerFactory.getLogger(AuditProducer.class);

  public static void main(String[] args) throws InterruptedException, ExecutionException {

    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092,localhost:9093");
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("acks", "all"); // #B <2>
    props.put("retries", "3"); // #C <3>
    props.put("max.in.flight.requests.per.connection", "1");

    try (Producer<String, String> producer = new KafkaProducer<>(props)) {
      ProducerRecord<String, String> producerRecord = new ProducerRecord<>("audit", null,
                                                                           "audit event");

      RecordMetadata result = producer.send(producerRecord).get();
      log.info("offset = {}, topic = {}, timestamp = {}", result.offset(), result.topic(), result.timestamp());

    }
  }

}
