package com.kafkainaction.producer;

import java.util.Calendar;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.kafkainaction.Alert;
import com.kafkainaction.alert_status;


public class HelloWorldProducer {

	public static void main(String[] args) {

		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092,localhost:9093");
		props.put("key.serializer", "org.apache.kafka.common.serialization.LongSerializer");
		props.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
		props.put("schema.registry.url", "http://localhost:8081");


		Producer<Long, Alert> producer = new KafkaProducer<Long, Alert>(props);
		Alert alert = new Alert();
		alert.setSensorId(12345L);
		alert.setTime(Calendar.getInstance().getTimeInMillis());
		alert.setStatus(alert_status.Critical);
		System.out.println(alert.toString());

		ProducerRecord<Long, Alert> producerRecord = new ProducerRecord<Long, Alert>("avrotest", alert.getSensorId(),
				alert);

		producer.send(producerRecord);

		producer.close();
	}

}
