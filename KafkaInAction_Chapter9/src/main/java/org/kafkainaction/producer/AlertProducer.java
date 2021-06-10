package org.kafkainaction.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.kafkainaction.callback.AlertCallback;
import org.kafkainaction.model.Alert;

import java.util.Properties;

public class AlertProducer {

  public static void main(String[] args) {

    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092,localhost:9093");
    props.put("key.serializer", "org.kafkainaction.serde.AlertKeySerde");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("interceptor.classes", "org.kafkainaction.producer.AlertProducerMetricsInterceptor");

    try (Producer<Alert, String> producer = new KafkaProducer<Alert, String>(props)) {

      Alert alert = new Alert(1, "Stage 1", "CRITICAL", "Stage 1 stopped");
      var producerRecord = new ProducerRecord<>("alert", alert, alert.getAlertMessage()); //<1>
      producer.send(producerRecord, new AlertCallback());
    }
  }

}
