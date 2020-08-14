package com.kafkainaction.consumer;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

public class KafkaConsumerThread implements Runnable {
    private final AtomicBoolean stopping = new AtomicBoolean(false);
    
    private static KafkaConsumer<String, String> consumer = null;
    
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092,localhost:9093");
		props.put("group.id", "helloconsumer");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		consumer = new KafkaConsumer<String, String>(props);
		consumer.subscribe(Arrays.asList("helloworld"));

		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(100);
			for (ConsumerRecord<String, String> record : records)
				System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
		}

		// consumer.close(); //unreachable code
	}  
		
    public void run() {
        try {
            consumer.subscribe(Arrays.asList("webclicks")); 
            while (!stopping.get()) { 
                ConsumerRecords<String, String> records = consumer.poll(100);
                //TODO - ADD CUSTOM CODE
            }
        } catch (WakeupException e) { 
            if (!stopping.get()) throw e;
        } finally {
            consumer.close();
        }
    }

    public void shutdown() { 
        stopping.set(true);
        consumer.wakeup();
    }
}
