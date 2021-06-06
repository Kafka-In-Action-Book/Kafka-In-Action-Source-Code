package org.kafkainaction.partitioner;

import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import org.kafkainaction.model.Alert;

public class AlertLevelPartitioner implements Partitioner { // #A <1>

	public int partition(final String topic, final Object objectKey, final byte[] keyBytes, final Object value,
			final byte[] valueBytes, final Cluster cluster) {

		final List<PartitionInfo> partitionMetaList = cluster.availablePartitionsForTopic(topic);
		final int partitionMod = partitionMetaList.size() - 1;
		final int criticalPartition = 0;

		final String key = ((Alert) objectKey).getAlertLevel();

		return key.contains("CRITICAL") ? criticalPartition : Math.abs(key.hashCode()) % partitionMod;
	}

	public void configure(Map<String, ?> configs) {
		// nothing needed

	}

	public void close() {
		// nothing needed

	}

}
