package org.kafkainaction.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.kafkainaction.model.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AlertConsumer {

  final static Logger log = LoggerFactory.getLogger(AlertConsumer.class);
  private volatile boolean keepConsuming = true;
  public static final String TOPIC_NAME = "alert";

  public static void main(String[] args) {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092,localhost:9093");
    props.put("enable.auto.commit", "false");
    props.put("group.id", "alert");
    /** Deserialize key using {@link org.kafkainaction.serde.AlertKeySerde} */
    props.put("key.deserializer", "org.kafkainaction.serde.AlertKeySerde");
    props.put("value.deserializer",
              "org.apache.kafka.common.serialization.StringDeserializer");

    AlertConsumer consumer = new AlertConsumer();
    consumer.consume(props);

    Runtime.getRuntime().addShutdownHook(new Thread(consumer::shutdown));
  }

  private void shutdown() {
    keepConsuming = false;
  }

  private void consume(final Properties props) {
    KafkaConsumer<Alert, String> consumer = new KafkaConsumer<>(props);
    TopicPartition partitionOne = new TopicPartition(TOPIC_NAME, 1);
    consumer.assign(Collections.singletonList(partitionOne));

    while (keepConsuming) {
      ConsumerRecords<Alert, String> records = consumer.poll(Duration.ofMillis(100));
      for (ConsumerRecord<Alert, String> record : records) {
        log.info("offset = {}, key = {}, value = {}",
                 record.offset(),
                 record.key().getStageId(),
                 record.value());
        commitOffset(record.offset(), record.partition(), TOPIC_NAME, consumer);
      }
    }
  }

  public static void commitOffset(long offset, int part, String topic, KafkaConsumer<Alert, String> consumer) {
    OffsetAndMetadata offsetMeta = new OffsetAndMetadata(offset + 1, "");

    Map<TopicPartition, OffsetAndMetadata> offsetMap = new HashMap<>();
    offsetMap.put(new TopicPartition(topic, part), offsetMeta);

    consumer.commitAsync(offsetMap, AlertConsumer::onComplete);
  }

  private static void onComplete(Map<TopicPartition, OffsetAndMetadata> map,
                                 Exception e) {
    if (e != null) {
      for (TopicPartition key : map.keySet()) {
        log.info("Commit failed: topic {}, partition {}, offset {}",
                 key.topic(),
                 key.partition(),
                 map.get(key).offset());
      }
    } else {
      for (TopicPartition key : map.keySet()) {
        log.info("OK: topic {}, partition {}, offset {}",
                 key.topic(),
                 key.partition(),
                 map.get(key).offset());
      }
    }
  }

}
