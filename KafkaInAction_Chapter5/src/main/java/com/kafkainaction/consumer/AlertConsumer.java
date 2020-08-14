package com.kafkainaction.consumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;

import com.kafkainaction.model.Alert;

public class AlertConsumer {

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092,localhost:9093");
		props.put("enable.auto.commit", "false");
		props.put("group.id", "alert"); 
		props.put("key.deserializer", "com.kafkainaction.serde.AlertKeySerde");
		props.put("value.deserializer", 
				"org.apache.kafka.common.serialization.StringDeserializer");
		
		KafkaConsumer<Alert, String> consumer = new KafkaConsumer<Alert, String>(props); 
		String topicName = "alert";
		TopicPartition partitionOne = new TopicPartition(topicName, 1);
		consumer.assign(Arrays.asList(partitionOne));
		
		while (true) {
		    ConsumerRecords<Alert, String> records = consumer.poll(Duration.ofMillis(100));
		    for (ConsumerRecord<Alert, String> record : records) {
		        System.out.printf("offset = %d, key = %s, value = %s", 
		        record.offset(), record.key().getStageId(), record.value());
		        commitOffset(record.offset(), record.partition(), topicName, consumer);
		    }
		}

	}
	
	public static void commitOffset(long offset,int part, String topic, KafkaConsumer<Alert, String> consumer) {
	    OffsetAndMetadata offsetMeta = new OffsetAndMetadata(offset + 1, "");

	    Map<TopicPartition, OffsetAndMetadata> offsetMap = new HashMap<TopicPartition, OffsetAndMetadata>();
	    offsetMap.put(new TopicPartition(topic, part), offsetMeta);

	    OffsetCommitCallback callback = new OffsetCommitCallback() { 
	     
	        public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) { 
	            if (e != null) {
	            	for (TopicPartition key: map.keySet()){
	            		System.out.printf("Commit failed: topic %s, partition %d, offset %d", key.topic(), key.partition(), map.get(key).offset() );
	            	}
	            }
	            else {
	            	for (TopicPartition key: map.keySet()){
            		  System.out.printf("OK: topic %s, partition %d, offset %d", key.topic(), key.partition(), map.get(key).offset() );
	            	}
	            }
	        }
	    };
	    consumer.commitAsync(offsetMap, callback);
	}

}
