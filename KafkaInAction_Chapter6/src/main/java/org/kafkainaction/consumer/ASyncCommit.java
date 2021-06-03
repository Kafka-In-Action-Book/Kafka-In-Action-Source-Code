package org.kafkainaction.consumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;

public class ASyncCommit {
	
	final static Logger log = LoggerFactory.getLogger(ASyncCommit.class);

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092,localhost:9093");
		props.put("group.id", "helloconsumer"); 
		props.put("key.deserializer", 
				"org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", 
				"org.apache.kafka.common.serialization.StringDeserializer");
		
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props); 
																							
		String topic = "webclicks";
		TopicPartition partition0 = new TopicPartition(topic, 0);
		TopicPartition partition1 = new TopicPartition(topic, 1);
		consumer.assign(Arrays.asList(partition0, partition1));
		
		while (true) {
		    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
		    for (ConsumerRecord<String, String> record : records) {
		        log.info("offset = {}, key = {}, value = {}", record.offset(), record.key(), record.value());
		        commitOffset(record.offset(),record.partition(), topic, consumer);
		    }
		}
		
	}
	
	public static void commitOffset(long offset,int part, String topic, KafkaConsumer<String, String> consumer) {
	    OffsetAndMetadata offsetMeta = new OffsetAndMetadata(offset + 1, "");

	    Map<TopicPartition, OffsetAndMetadata> offsetMap = new HashMap<>();
	    offsetMap.put(new TopicPartition(topic, part), offsetMeta);

	    OffsetCommitCallback callback = (map, e) -> { 
		if (e != null) {
			for (TopicPartition key: map.keySet()){
				log.info("Commit failed: topic %s, partition %d, offset %d", key.topic(), key.partition(), map.get(key).offset() );
			}
		}
		else {
			for (TopicPartition key: map.keySet()){
			  log.info("OK: topic %s, partition %d, offset %d", key.topic(), key.partition(), map.get(key).offset() );
			}
		}
	    };
	    consumer.commitAsync(offsetMap, callback);
	}
}
