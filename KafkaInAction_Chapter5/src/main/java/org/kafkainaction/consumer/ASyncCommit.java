package org.kafkainaction.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ASyncCommit {

  final static Logger log = LoggerFactory.getLogger(ASyncCommit.class);
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

    final ASyncCommit aSyncCommit = new ASyncCommit();
    aSyncCommit.consume(props);
    Runtime.getRuntime().addShutdownHook(new Thread(aSyncCommit::shutdown));
  }

  private void consume(final Properties props) {
    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {

      consumer.assign(List.of(new TopicPartition(TOPIC_NAME, 1),
                              new TopicPartition(TOPIC_NAME, 2)));

      while (keepConsuming) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, String> record : records) {
          log.info("offset = {}, key = {}, value = {}", record.offset(), record.key(), record.value());
          commitOffset(record.offset(), record.partition(), TOPIC_NAME, consumer);
        }
      }
    }
  }

  public static void commitOffset(long offset, 
                                  int partition, 
                                  String topic, 
                                  KafkaConsumer<String, String> consumer) {
    OffsetAndMetadata offsetMeta = new OffsetAndMetadata(offset + 1, "");

    Map<TopicPartition, OffsetAndMetadata> offsetMap = new HashMap<>();
    offsetMap.put(new TopicPartition(topic, partition), offsetMeta);

    consumer.commitAsync(offsetMap, (map, e) -> {
      if (e != null) {
        for (TopicPartition key : map.keySet()) {
          log.info("Commit failed: topic {}, partition {}, offset {}", key.topic(), key.partition(), map.get(key).offset());
        }
      } else {
        for (TopicPartition key : map.keySet()) {
          log.info("OK: topic {}, partition {}, offset {}", key.topic(), key.partition(), map.get(key).offset());
        }
      }
    });
  }

  private void shutdown() {
    keepConsuming = false;
  }
}
