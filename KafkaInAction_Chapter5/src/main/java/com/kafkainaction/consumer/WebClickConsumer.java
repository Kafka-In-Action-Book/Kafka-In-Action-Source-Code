package com.kafkainaction.consumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class WebClickConsumer {

  final static Logger log = LoggerFactory.getLogger(WebClickConsumer.class);

  private volatile boolean keepConsuming = true;

  public static void main(String[] args) {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092,localhost:9093,,localhost:9094");
    props.put("group.id", "webconsumer");
    props.put("enable.auto.commit", "true");
    props.put("auto.commit.interval.ms", "1000");
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

    WebClickConsumer webClickConsumer = new WebClickConsumer();
    webClickConsumer.consume(props);

    Runtime.getRuntime().addShutdownHook(new Thread(webClickConsumer::shutdown));
  }

  private void consume(Properties props) {
    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
      consumer.subscribe(Collections.singletonList("webclicks"));

      while (keepConsuming) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, String> record : records) {
          log.info("[Consumer Record] offset = {}, key = {}, value = {}", record.offset(), record.key(),
                   record.value());
	  log.info("value = %.2f%n", Double.parseDouble(record.value()) * 1.543);	
        }
      }
    }
  }

  private void shutdown() {
    keepConsuming = false;
  }
}
