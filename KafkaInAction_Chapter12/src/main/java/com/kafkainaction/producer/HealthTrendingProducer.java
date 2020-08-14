package com.kafkainaction.producer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.kafkainaction.model.Alert;

public class HealthTrendingProducer {
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092,localhost:9093");
		props.put("key.serializer", "com.kafkainaction.serde.AlertKeySerde");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		Producer<Alert, String> producer = new KafkaProducer<Alert, String>(props);
		Alert alert = new Alert(0, "Stage 0", "CRITICAL", "Stage 0 stopped");
		ProducerRecord<Alert, String> producerRecord = new ProducerRecord<Alert, String>("healthtrend", alert, alert.getAlertMessage()); // #A <1>
		 

		RecordMetadata result = producer.send(producerRecord).get();
		System.out.printf("offset = %d, topic = %s, timestamp = %Tc %n", result.offset(), result.topic(), result.timestamp());


		producer.close();
	}

}
