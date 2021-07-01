package org.kafkainaction.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
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

public class AuditConsumer {

  final static Logger log = LoggerFactory.getLogger(AuditConsumer.class);
  private volatile boolean keepConsuming = true;

  public static void main(String[] args) {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092,localhost:9093");
    props.put("enable.auto.commit", "false");
    props.put("group.id", "audit");
    props.put("key.deserializer",
              "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("value.deserializer",
              "org.apache.kafka.common.serialization.StringDeserializer");

    final AuditConsumer auditConsumer = new AuditConsumer();
    auditConsumer.consume(props);
    Runtime.getRuntime().addShutdownHook(new Thread(auditConsumer::shutdown));
  }

  private void consume(final Properties props) {
    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {

      consumer.subscribe(List.of("kinaction_audit"));

      while (keepConsuming) {
        var records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, String> record : records) {
          log.info("offset = {}, key = {}, value = {}",
                   record.offset(), record.key(), record.value());

          OffsetAndMetadata offsetMeta = new OffsetAndMetadata(record.offset() + 1, "");

          Map<TopicPartition, OffsetAndMetadata> offsetMap = new HashMap<>();
          offsetMap.put(new TopicPartition("audit", record.partition()), offsetMeta);

          consumer.commitSync(offsetMap);
        }
      }
    }

  }

  private void shutdown() {
    keepConsuming = false;
  }
}
