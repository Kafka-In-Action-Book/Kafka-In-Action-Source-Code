package com.kafkainaction.producer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class AuditProducer {
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092,localhost:9093");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("acks", "all"); // #B <2>
		props.put("retries", "3"); // #C <3>
		props.put("max.in.flight.requests.per.connection", "1");

		Producer<String, String> producer = new KafkaProducer<String, String>(props);

		ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("audit", null,
				"audit event");

		RecordMetadata result = producer.send(producerRecord).get();
		System.out.printf("offset = %d, topic = %s, timestamp = %Tc %n", result.offset(), result.topic(), result.timestamp());


		producer.close();
	}

}
