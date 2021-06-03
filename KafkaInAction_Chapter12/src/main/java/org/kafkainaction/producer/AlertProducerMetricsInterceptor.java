package org.kafkainaction.producer;

import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.kafkainaction.model.Alert;

public class AlertProducerMetricsInterceptor implements ProducerInterceptor<Alert, String>{
	final static Logger log = LoggerFactory.getLogger(AlertProducerMetricsInterceptor.class);

	public ProducerRecord<Alert, String> onSend(ProducerRecord<Alert, String> record) {
		Headers headers = record.headers();
		String traceId = UUID.randomUUID().toString();
		headers.add("traceId", traceId.getBytes());
		log.info("Created traceId: " + traceId);
		return record;
	}

	public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
		if (exception != null) {
			log.info("producer send exception " + exception.getMessage());
		} else {
			log.info("ack'ed topic={}, partition={}, offset={}",
                    metadata.topic(), metadata.partition(), metadata.offset());
		}
		
	}

	public void close() {		
	}
	public void configure(Map<String, ?> configs) {		
	}

}
