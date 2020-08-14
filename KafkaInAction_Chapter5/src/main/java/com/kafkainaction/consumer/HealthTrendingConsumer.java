package com.kafkainaction.consumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.kafkainaction.model.Alert;

public class HealthTrendingConsumer {

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092,localhost:9093");
		props.put("group.id", "healthtrend");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("key.deserializer", "com.kafkainaction.serde.AlertKeySerde");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		@SuppressWarnings("resource")
		KafkaConsumer<Alert, String> consumer = new KafkaConsumer<Alert, String>(props);
		consumer.subscribe(Arrays.asList("healthtrend"));

		while (true) {
			ConsumerRecords<Alert, String> records = consumer.poll(Duration.ofMillis(100));
			for (ConsumerRecord<Alert, String> record : records) {
				System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key().getStageId(), record.value());
			}
		}

		// consumer.close(); //unreachable code
	}

}
