package org.kafkainaction.consumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.kafkainaction.model.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlertConsumer {

  final static Logger log = LoggerFactory.getLogger(AlertConsumer.class);
  public static final String TOPIC_NAME = "alert";


  @SuppressWarnings("resource")
  public ConsumerRecords<Alert, String> getAlertMessages(Properties consumerConfig) {
  	KafkaConsumer<Alert, String> consumer = new KafkaConsumer<>(consumerConfig);
  	consumer.subscribe(Collections.singletonList("alert"));
  	return consumer.poll(Duration.ofMillis(1000));
  }

}
