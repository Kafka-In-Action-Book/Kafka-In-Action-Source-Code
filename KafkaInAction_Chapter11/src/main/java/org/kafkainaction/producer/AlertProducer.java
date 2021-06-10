package org.kafkainaction.producer;

import com.kakfainaction.avro.Alert;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Properties;

import static com.kakfainaction.avro.AlertStatus.Critical;

public class AlertProducer {

  final static Logger log = LoggerFactory.getLogger(AlertProducer.class);

  public static void main(String[] args) {

    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092,localhost:9093");
    props.put("key.serializer", "org.apache.kafka.common.serialization.LongSerializer");
    props.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer"); // <1>
    props.put("schema.registry.url", "http://localhost:8081"); // <2>

    try (Producer<Long, Alert> producer = new KafkaProducer<>(props)) {
      Alert alert = new Alert(); //<3>
      alert.setSensorId(12345L);
      alert.setTime(Calendar.getInstance().getTimeInMillis());
      alert.setStatus(Critical);
      log.info(alert.toString());

      ProducerRecord<Long, Alert> producerRecord = new ProducerRecord<>("avrotest", alert.getSensorId(), alert); // <4>

      producer.send(producerRecord);
    }

  }

}
