package com.kafkainaction.consumer;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.kafkainaction.Alert;

public class HelloWorldConsumer {

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092,localhost:9093");
		props.put("group.id", "helloconsumer");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");
		props.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
		props.put("schema.registry.url", "http://localhost:8081");

		@SuppressWarnings("resource")
		KafkaConsumer<Long, Alert> consumer = new KafkaConsumer<Long, Alert>(props);
		consumer.subscribe(Arrays.asList("avrotest"));

		while (true) {
			ConsumerRecords<Long, Alert> records = consumer.poll(100);
			for (ConsumerRecord<Long, Alert> record : records)
				System.out.printf("offset = %d, key = %d, value = %s%n", record.offset(), record.key(), record.value());
		}

		// consumer.close(); //unreachable code
	}

}
