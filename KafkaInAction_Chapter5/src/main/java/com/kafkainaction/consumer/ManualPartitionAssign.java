package com.kafkainaction.consumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.TopicPartition;

public class ManualPartitionAssign {

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092,localhost:9093");
		props.put("group.id", "helloconsumer"); 
		props.put("key.deserializer", 
				"org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", 
				"org.apache.kafka.common.serialization.StringDeserializer");
		
		@SuppressWarnings("resource")
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props); 
																							
		String topicName = "webviews";
		TopicPartition partitionOne = new TopicPartition(topicName, 1);
		TopicPartition partitionTwo = new TopicPartition(topicName, 2);
		consumer.assign(Arrays.asList(partitionOne, partitionTwo));
					
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100)); 
																			
			for (ConsumerRecord<String, String> record : records) {
				System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
				System.out.printf("value = %d%n", Integer.getInteger(record.value()) * 1.543);
			}
		
			// consumer.close(); //unreachable code
		}

	}

}
