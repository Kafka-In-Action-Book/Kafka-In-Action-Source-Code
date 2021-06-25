package org.kafkainaction.consumer;

import org.kafkainaction.avro.Alert;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;

public class AlertConsumer {

  final static Logger log = LoggerFactory.getLogger(AlertConsumer.class);

  public static void main(String[] args) {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("group.id", "alertinterceptor");
    props.put("enable.auto.commit", "true");
    props.put("auto.commit.interval.ms", "1000");
    props.put("key.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");
    props.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer"); // <1>
    props.put("schema.registry.url", "http://localhost:8081"); // <2>
    props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

    KafkaConsumer<Long, Alert> consumer = new KafkaConsumer<Long, Alert>(props); //C <3>

    consumer.subscribe(List.of("avrotest")); //<4>

    while (true) {
      ConsumerRecords<Long, Alert> records = consumer.poll(Duration.ofMillis(100));
      for (ConsumerRecord<Long, Alert> record : records) {
        log.info("Alert Content = {}", record.value().toString()); //<5>
      }
    }
  }

}
