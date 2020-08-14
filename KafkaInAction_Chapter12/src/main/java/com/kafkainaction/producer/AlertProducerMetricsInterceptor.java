package com.kafkainaction.producer;

import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Headers;

import com.kafkainaction.model.Alert;

public class AlertProducerMetricsInterceptor implements ProducerInterceptor<Alert, String>{


	public ProducerRecord<Alert, String> onSend(ProducerRecord<Alert, String> record) {
		Headers headers = record.headers();
		String traceId = UUID.randomUUID().toString();
		headers.add("traceId", traceId.getBytes());
		System.out.println("Created traceId: " + traceId);
		return record;
	}

	public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
		if (exception != null) {
			System.out.println("producer send exception " + exception.getMessage());
		} else {
			System.out.println(String.format("ack'ed topic=%s, partition=%d, offset=%d\n",
                    metadata.topic(), metadata.partition(), metadata.offset()));
		}
		
	}

	public void close() {		
	}
	public void configure(Map<String, ?> configs) {		
	}

}
