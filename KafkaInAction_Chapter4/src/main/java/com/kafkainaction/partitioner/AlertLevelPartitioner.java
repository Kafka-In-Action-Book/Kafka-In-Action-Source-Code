package com.kafkainaction.partitioner;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import com.kafkainaction.model.Alert;

public class AlertLevelPartitioner implements Partitioner { // #A <1>

    public int partition(final String topic,
                         final Object objectKey,
                         final byte[] keyBytes,
                         final Object value,
                         final byte[] valueBytes,
                         final Cluster cluster) {

        final List<PartitionInfo> partitionInfoList =
                cluster.availablePartitionsForTopic(topic);
        final int partitionSize = partitionInfoList.size();
        final int criticalPartition = partitionSize - 1;
        final int partitionCount = partitionSize- 1;

        final String key = ((Alert) objectKey).getAlertLevel();

        if (key.contains("CRITICAL")) { // #B <2>
            return criticalPartition;
        } else {
            return Math.abs(key.hashCode()) % partitionCount;
        }

    }

	public void configure(Map<String, ?> configs) {
		// nothing needed
		
	}

	public void close() {
		// nothing needed
		
	}


}
