package com.kafkainaction.consumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

public class KafkaConsumerThread implements Runnable {
    private final KafkaConsumer<String, String> consumer;
    private final AtomicBoolean stopping = new AtomicBoolean(false);

    public KafkaConsumerThread(KafkaConsumer<String, String> consumer) {
      this.consumer = consumer;
    }

    public void run() {
        try {
            consumer.subscribe(Arrays.asList("webclicks")); 
            while (!stopping.get()) { 
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
    			for (ConsumerRecord<String, String> record : records) {
    				System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
    			}
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
