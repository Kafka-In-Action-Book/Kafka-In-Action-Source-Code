package com.kafkainaction.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class HelloWorldProducer {

  public static void main(String[] args) {

    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092,localhost:9093");
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    try (Producer<String, String> producer = new KafkaProducer<>(props)) {

      ProducerRecord<String, String> producerRecord = new ProducerRecord<>("helloworld", null,
                                                                           "hello world again!");
      producer.send(producerRecord);
    }
  }

}
