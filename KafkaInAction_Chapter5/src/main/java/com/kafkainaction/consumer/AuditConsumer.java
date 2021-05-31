package com.kafkainaction.consumer;

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
import org.apache.kafka.common.TopicPartition;

public class AuditConsumer {
        final static Logger log = LoggerFactory.getLogger(AuditConsumer.class);

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092,localhost:9093");
		props.put("enable.auto.commit", "false");
		props.put("group.id", "audit"); 
		props.put("key.deserializer", 
				"org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", 
				"org.apache.kafka.common.serialization.StringDeserializer");
		
		@SuppressWarnings("resource")
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props); 
		
		consumer.subscribe(Arrays.asList("audit"));
		
		while (true) {
		    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
		    for (ConsumerRecord<String, String> record : records) {
		        log.info("offset = %d, key = %s, value = %s", 
		        record.offset(), record.key(), record.value());
		        
			    OffsetAndMetadata offsetMeta = new OffsetAndMetadata(record.offset() + 1, "");

			    Map<TopicPartition, OffsetAndMetadata> offsetMap = new HashMap<TopicPartition, OffsetAndMetadata>();
			    offsetMap.put(new TopicPartition("audit", record.partition()), offsetMeta);
			    
		        consumer.commitSync(offsetMap);
		    }
		}

	}

}
