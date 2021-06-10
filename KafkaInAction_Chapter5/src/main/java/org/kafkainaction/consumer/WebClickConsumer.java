package org.kafkainaction.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class WebClickConsumer {

  final static Logger log = LoggerFactory.getLogger(WebClickConsumer.class);
  private volatile boolean keepConsuming = true;

  public static void main(String[] args) {
    Properties props = new Properties();
    props.put("bootstrap.servers",
              "localhost:9092,localhost:9093,,localhost:9094");
    props.put("group.id", "webconsumer");   //<1>
    props.put("enable.auto.commit", "true");
    props.put("auto.commit.interval.ms", "1000");
    props.put("key.deserializer",
              "org.apache.kafka.common.serialization.StringDeserializer");    //<2>
    props.put("value.deserializer",
              "org.apache.kafka.common.serialization.StringDeserializer");

    WebClickConsumer webClickConsumer = new WebClickConsumer();
    webClickConsumer.consume(props);

    Runtime.getRuntime().addShutdownHook(new Thread(webClickConsumer::shutdown));
  }

  private void consume(Properties props) {
    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {   //<3>
      consumer.subscribe(Collections.singletonList("webclicks"));   //<4>

      while (keepConsuming) {   //<5>
        var records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, String> record : records) {
          log.info("[Consumer Record] offset = {}, key = {}, value = {}",
                   record.offset(), record.key(), record.value());
          log.info("value = {}", Double.parseDouble(record.value()) * 1.543);
        }
      }
    }
  }

  private void shutdown() {
    keepConsuming = false;
  }
}