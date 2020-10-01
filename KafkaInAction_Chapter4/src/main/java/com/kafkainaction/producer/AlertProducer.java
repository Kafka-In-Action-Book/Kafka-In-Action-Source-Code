package com.kafkainaction.producer;

import com.kafkainaction.callback.AlertCallback;
import com.kafkainaction.model.Alert;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class AlertProducer {

  public static void main(String[] args) {

    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092,localhost:9093");
    props.put("key.serializer", "com.kafkainaction.serde.AlertKeySerde"); // #A <1>
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("partitioner.class",
              "com.kafkainaction.partitioner.AlertLevelPartitioner"); // #B <2>

    try (Producer<Alert, String> producer = new KafkaProducer<>(props)) {
      Alert alert = new Alert(1, "Stage 1", "CRITICAL", "Stage 1 stopped");
      ProducerRecord<Alert, String>
          producerRecord = new ProducerRecord<>("alert", alert, alert.getAlertMessage()); // #C <3>

      producer.send(producerRecord, new AlertCallback());
    }
  }

}
