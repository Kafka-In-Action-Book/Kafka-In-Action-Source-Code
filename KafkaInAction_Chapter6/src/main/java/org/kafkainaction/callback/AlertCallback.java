package org.kafkainaction.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

public class AlertCallback implements Callback{
	final static Logger log = LoggerFactory.getLogger(AlertCallback.class);

		public void onCompletion(RecordMetadata metadata, Exception exception) {
            if(exception != null){
        		log.info("Error sending message: "+ "offset = {}, topic = {}, timestamp = {}", metadata.offset(), metadata.topic(), metadata.timestamp());
            }
			
		}
}
