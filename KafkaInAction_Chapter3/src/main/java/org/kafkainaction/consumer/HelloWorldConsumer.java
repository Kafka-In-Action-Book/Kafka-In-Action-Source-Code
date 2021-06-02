package org.kafkainaction.consumer;

import org.kafkainaction.Alert;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class HelloWorldConsumer {

  final static Logger log = LoggerFactory.getLogger(HelloWorldConsumer.class);

  private volatile boolean keepConsuming = true;

  public static void main(String[] args) {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("group.id", "helloconsumer");
    props.put("enable.auto.commit", "true");
    props.put("auto.commit.interval.ms", "1000");
    props.put("key.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");
    props.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
    props.put("schema.registry.url", "http://localhost:8081");

    HelloWorldConsumer helloWorldConsumer = new HelloWorldConsumer();
    helloWorldConsumer.consume(props);

    Runtime.getRuntime().addShutdownHook(new Thread(helloWorldConsumer::shutdown));
  }

  private void consume(Properties props) {
    try (KafkaConsumer<Long, Alert> consumer = new KafkaConsumer<>(props)) {
      consumer.subscribe(Collections.singletonList("avrotest"));

      while (keepConsuming) {
        ConsumerRecords<Long, Alert> records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<Long, Alert> record : records) {
          log.info("[Consumer Record] offset = {}, key = {}, value = {}",
                   record.offset(),
                   record.key(),
                   record.value());
        }
      }
    }
  }

  private void shutdown() {
    keepConsuming = false;
  }

}
