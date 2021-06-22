package org.kafkainaction.partition;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;

public class S3Offsets implements ConsumerRebalanceListener {  //<1>

  private final KafkaConsumer<String, String> consumer;

  public void onPartitionsRevoked(Collection<TopicPartition> partitions) {    //<2>
    storeInS3(partitions);
  }

  public void onPartitionsAssigned(Collection<TopicPartition> partitions) {   //<3>
    getFromS3(partitions);
  }
  
  private void getFromS3(Collection<TopicPartition> partitions) {
    for (TopicPartition partition : partitions) {
      consumer.seek(partition, readOffsetFromStorage(partition));
    }
  }
  
  private void storeInS3(Collection<TopicPartition> partitions) {
    for (TopicPartition partition : partitions) {
      saveOffsetInStorage(consumer.position(partition));
    }
  }

  private long readOffsetFromStorage(TopicPartition partition) {
    // ADD YOUR CUSTOM LOGIC HERE
    return 0;
  }

  private void saveOffsetInStorage(long position) {
    // ADD YOUR CUSTOM LOGIC HERE	
  }

  public S3Offsets(KafkaConsumer<String, String> consumer) {
    super();
    this.consumer = consumer;
  }
}
