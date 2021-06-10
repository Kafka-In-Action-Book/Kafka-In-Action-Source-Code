package org.kafkainaction.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class ManualPartitionAssign {

  final static Logger log = LoggerFactory.getLogger(ManualPartitionAssign.class);
  private volatile boolean keepConsuming = true;
  public static final String TOPIC_NAME = "webviews";

  public static void main(String[] args) {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092,localhost:9093");
    props.put("group.id", "helloconsumer");
    props.put("key.deserializer",
              "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("value.deserializer",
              "org.apache.kafka.common.serialization.StringDeserializer");

    ManualPartitionAssign manualPartitionAssign = new ManualPartitionAssign();
    manualPartitionAssign.consume(props);
    Runtime.getRuntime().addShutdownHook(new Thread(manualPartitionAssign::shutdown));

  }

  private void shutdown() {
    keepConsuming = false;
  }

  private void consume(Properties props) {
    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {

      consumer.assign(List.of(new TopicPartition(TOPIC_NAME, 1),
                              new TopicPartition(TOPIC_NAME, 2)));

      while (keepConsuming) {
        var records = consumer.poll(Duration.ofMillis(100));

        for (ConsumerRecord<String, String> record : records) {
          log.info("offset = {}, key = {}, value = {}", record.offset(), record.key(), record.value());
          log.info("value = {}", Integer.getInteger(record.value()) * 1.543);
        }
      }
    }
  }
}
