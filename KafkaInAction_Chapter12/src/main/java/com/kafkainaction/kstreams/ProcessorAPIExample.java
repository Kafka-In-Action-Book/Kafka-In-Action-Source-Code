package com.kafkainaction.kstreams;

import static org.apache.kafka.streams.Topology.AutoOffsetReset.LATEST;

import java.util.Properties;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

public class ProcessorAPIExample {

	public static void main(String[] args) throws Exception {

		Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "ProcessorAPIExample");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

		final Serde<String> stringSerde = Serdes.String();
		Deserializer<String> stringDeserializer = stringSerde.deserializer();
		Serializer<String> stringSerializer = stringSerde.serializer();

		Topology topology = new Topology();
		topology = topology.addSource(LATEST, "input", stringDeserializer, stringDeserializer, "input-topic");

		topology = topology.addProcessor("testProcessor", () -> new TestProcessor(), "input");

		topology = topology.addSink("Output-Sink1", "sink-topic1", stringSerializer, stringSerializer, "testProcessor");

		topology = topology.addSink("Output-Sink2", "sink-topic2", stringSerializer, stringSerializer, "testProcessor");

		KafkaStreams kafkaStreams = new KafkaStreams(topology, props);
		kafkaStreams.cleanUp();
		kafkaStreams.start();
		Thread.sleep(5000);
		kafkaStreams.close();
	}

}
