package org.kafkainaction.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.kafkainaction.callback.AlertCallback;
import org.kafkainaction.model.Alert;
import org.kafkainaction.partitioner.AlertLevelPartitioner;
import org.kafkainaction.serde.AlertKeySerde;

import java.util.Properties;

public class AlertProducer {

  public static void main(String[] args) {

    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092,localhost:9093");
    
    props.put("key.serializer", AlertKeySerde.class.getName());   //<1>
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    /** Use {@link org.kafkainaction.partitioner.AlertLevelPartitioner} to determine partition */
    props.put("partitioner.class", AlertLevelPartitioner.class.getName());    //<2>

    try (Producer<Alert, String> producer = new KafkaProducer<>(props)) {
      Alert alert = new Alert(1, "Stage 1", "CRITICAL", "Stage 1 stopped");
      ProducerRecord<Alert, String>
          producerRecord = new ProducerRecord<>("kinaction_alert", alert, alert.getAlertMessage());   //<3>

      producer.send(producerRecord, new AlertCallback());
    }
  }

}
