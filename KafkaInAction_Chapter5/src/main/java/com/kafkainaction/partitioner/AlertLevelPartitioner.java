package com.kafkainaction.partitioner;
import java.nio.charset.StandardCharsets;
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
        final int partitionCount = partitionSize;

        final String alertLevel = new String(valueBytes, StandardCharsets.UTF_8);

        if (alertLevel.equalsIgnoreCase("CRITICAL")) { // #B <2>
            return criticalPartition;
        } else {
        	String stageId = ((Alert) objectKey).getStageId();
            return Math.abs(stageId.hashCode()) % partitionCount;
        }

    }

	public void configure(Map<String, ?> configs) {
		// nothing needed
		
	}

	public void close() {
		// nothing needed
		
	}


}
