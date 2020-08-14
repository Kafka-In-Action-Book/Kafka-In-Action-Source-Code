package com.kafkainaction.consumer;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

import com.kafkainaction.model.Alert;

public class AlertConsumerMetricsInterceptor implements ConsumerInterceptor<Alert, String> {

	public ConsumerRecords<Alert, String> onConsume(ConsumerRecords<Alert, String> records) {
		if (records.isEmpty()) {
			return records;
		} else {
			for (ConsumerRecord<Alert, String> record : records) {
				Headers headers = record.headers();
				for (Header header : headers) {
					if ("traceId".equals(header.key())) {
						System.out.println("TraceId is: " + new String(header.value()));
					}
				}
			}
		}
		
		return records;
	}

	public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {
	}

	public void close() {
	}

	public void configure(Map<String, ?> configs) {
	}


}
