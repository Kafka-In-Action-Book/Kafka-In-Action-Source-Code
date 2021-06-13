package org.kafkainaction.producer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class FlumeSinkProducer {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		Properties props = readConfig();
		
		String topic = props.getProperty("topic");
		props.remove("topic");
	    
		try (Producer<String, String> producer = new KafkaProducer<>(props)) {
	        @SuppressWarnings("unused")
			ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, null,
	                                                                             "event");
	      }		

	}

	private static Properties readConfig() {
		Path path = Paths.get("src/main/resources/kafkasink.conf");
		
	    Properties props = new Properties();
 
		try (Stream<String>  lines = Files.lines(path)) {
		    lines.forEachOrdered(line -> determineProperty(line, props));
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}
		return props;
	}

	private static void determineProperty(String line, Properties props) {
		if (line.contains("bootstrap")) {
			props.put("bootstrap.servers", line.split("=")[1]);
		} else if (line.contains("acks")) {
		    props.put("acks", line.split("=")[1]);  
		} else if (line.contains("compression.type")) {
			props.put("compression.type", line.split("=")[1]);
		} else if (line.contains("topic")) {
			props.put("topic", line.split("=")[1]);
		} 
		
		props.putIfAbsent("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.putIfAbsent("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		
	}

}
