package com.kafkainaction.partition;

import java.util.Collection;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

public class MaintainOffsetsOnRebalance 
implements ConsumerRebalanceListener { 
	
	private KafkaConsumer<String, String> consumer;

    public MaintainOffsetsOnRebalance(KafkaConsumer<String, String> consumer) {
		super();
		this.consumer = consumer;
	}

	public void onPartitionsRevoked(Collection<TopicPartition> partitions) { 
        
		for(TopicPartition partition: partitions)
           saveOffsetInStorage(consumer.position(partition));
    }

	public void onPartitionsAssigned(Collection<TopicPartition> partitions) { 
        for(TopicPartition partition: partitions)
           consumer.seek(partition, readOffsetFromStorage(partition));
    }

	private long readOffsetFromStorage(TopicPartition partition) {
		// ADD YOUR CUSTOM LOGIC HERE
		return 0;
	}
	
	private void saveOffsetInStorage(long position) {
		// ADD YOUR CUSTOM LOGIC HERE	
	}
}
