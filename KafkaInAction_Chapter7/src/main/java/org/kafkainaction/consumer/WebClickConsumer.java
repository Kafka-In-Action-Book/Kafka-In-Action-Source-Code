package org.kafkainaction.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebClickConsumer {

  final static Logger log = LoggerFactory.getLogger(WebClickConsumer.class);

  public static void main(String[] args) {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092,localhost:9093");
    props.put("group.id", "helloconsumer");
    props.put("key.deserializer",
              "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("value.deserializer",
              "org.apache.kafka.common.serialization.StringDeserializer");

    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {

      consumer.subscribe(Collections.singletonList("webclicks")); // D <4>

      while (true) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

        for (ConsumerRecord<String, String> record : records) {
          log.info("offset = {}, key = {}, value = {}", record.offset(),
                            record.key(), record.value());
          log.info("value = {}", Integer.getInteger(record.value()) * 1.543);
        }
      }
    }

  }

}
