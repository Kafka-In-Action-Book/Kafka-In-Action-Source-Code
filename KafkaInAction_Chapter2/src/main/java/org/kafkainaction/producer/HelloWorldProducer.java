package org.kafkainaction.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class HelloWorldProducer {

  public static void main(String[] args) {

    Properties producerProperties = new Properties();   //<1>
    producerProperties.put("bootstrap.servers",
                           "localhost:9092,localhost:9093,localhost:9094");   //<2>

    producerProperties.put(
        "key.serializer", "org.apache.kafka.common.serialization.StringSerializer");    //<3>
    producerProperties.put("value.serializer",
                           "org.apache.kafka.common.serialization.StringSerializer");

    try (Producer<String, String> producer = new KafkaProducer<>(producerProperties)) { //<4>

      ProducerRecord<String, String> producerRecord =
          new ProducerRecord<>("helloworld", null, "hello world again!");   //<5>

      producer.send(producerRecord);    //<6>
      producer.close();   //<7>
    }
  }
}
