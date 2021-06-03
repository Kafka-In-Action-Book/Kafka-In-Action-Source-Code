package org.kafkainaction.consumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

public class ManualPartitionAssign {

	final static Logger log = LoggerFactory.getLogger(ManualPartitionAssign.class);

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092,localhost:9093");
		props.put("group.id", "helloconsumer"); 
		props.put("key.deserializer", 
				"org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", 
				"org.apache.kafka.common.serialization.StringDeserializer");
		
		@SuppressWarnings("resource")
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props); 
																							
		String topic = "webclicks";
		TopicPartition partition0 = new TopicPartition(topic, 0);
		TopicPartition partition1 = new TopicPartition(topic, 1);
		consumer.assign(Arrays.asList(partition0, partition1));
		
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100)); 
																			
			for (ConsumerRecord<String, String> record : records) {
				log.info("offset = {}, key = {}, value = {}", record.offset(), record.key(), record.value());
				log.info("value = {}", Integer.getInteger(record.value()) * 1.543);
			}

			// consumer.close(); //unreachable code
		}

	}

}
