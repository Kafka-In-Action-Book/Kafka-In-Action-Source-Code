package com.kafkainaction.consumer;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

public class AutoSync {

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
																							
		String topic = "webclicks";
		TopicPartition partition0 = new TopicPartition(topic, 0);
		TopicPartition partition1 = new TopicPartition(topic, 1);
		consumer.assign(Arrays.asList(partition0, partition1));
		
		while (true) {
		    ConsumerRecords<String, String> records = consumer.poll(100);
		    for (ConsumerRecord<String, String> record : records) {
		        System.out.printf("offset = %d, key = %s, value = %s", 
		        record.offset(), record.key(), record.value());
		        consumer.commitSync();
		    }
		}

	}

}
