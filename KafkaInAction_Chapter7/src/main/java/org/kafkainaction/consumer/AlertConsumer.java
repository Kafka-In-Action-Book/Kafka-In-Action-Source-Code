package org.kafkainaction.consumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.kafkainaction.model.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlertConsumer {

  final static Logger log = LoggerFactory.getLogger(AlertConsumer.class);

  @SuppressWarnings("resource")
  public ConsumerRecords<Alert, String> getAlertMessages(Properties kaConsumerProperties) {
  	KafkaConsumer<Alert, String> consumer = new KafkaConsumer<>(kaConsumerProperties);
  	consumer.subscribe(List.of("kinaction_alert"));
  	return consumer.poll(Duration.ofMillis(2500));
  }

}
