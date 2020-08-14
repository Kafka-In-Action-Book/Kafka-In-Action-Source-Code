package com.kafkainaction.serde;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import com.kafkainaction.model.Alert;

public class AlertKeySerde implements Serializer<Alert>, Deserializer<Alert> {

	public void close() {
		// nothing needed
	}

	public void configure(Map<String, ?> configs, boolean isKey) {
		// nothing needed
	}

	public Alert deserialize(String topic, byte[] value) {
	    //We will leave this part for later
		return null;
	}

	public byte[] serialize(String topic, Alert value) {
		if (value == null) {
			return null;
		}
		
		try {
			return value.getStageId().getBytes("UTF8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		
	}

}
