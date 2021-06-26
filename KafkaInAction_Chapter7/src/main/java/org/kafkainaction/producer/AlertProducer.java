package org.kafkainaction.producer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.kafkainaction.model.Alert;

public class AlertProducer {

	public void sendMessage(Properties producerConfig) throws InterruptedException, ExecutionException {
		producerConfig.put("partitioner.class", "org.kafkainaction.partitioner.AlertLevelPartitioner"); // <2>

		try (Producer<Alert, String> producer = new KafkaProducer<>(producerConfig)) {
			Alert alert = new Alert(1, "Stage 1", "CRITICAL", "Stage 1 stopped");
			ProducerRecord<Alert, String> producerRecord = new ProducerRecord<>("alert", alert,
					alert.getAlertMessage()); // <3>

			producer.send(producerRecord).get();
		}

	}

}