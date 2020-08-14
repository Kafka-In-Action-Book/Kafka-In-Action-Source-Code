package com.kafkainaction.serde;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
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
		if (value == null) {
			return null;
		}

		Alert alert = new Alert();
		alert.setStageId(new String(value, StandardCharsets.UTF_8));
		return alert;
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
