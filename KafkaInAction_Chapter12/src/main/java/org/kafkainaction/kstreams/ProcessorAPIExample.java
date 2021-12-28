package org.kafkainaction.kstreams;

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

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {

		Properties kaProperties = new Properties();
		kaProperties.put(StreamsConfig.APPLICATION_ID_CONFIG, "Kinaction_ProcessorAPIExample");
		kaProperties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094");

		final Serde<String> stringSerde = Serdes.String();
		Deserializer<String> stringDeserializer = stringSerde.deserializer();
		Serializer<String> stringSerializer = stringSerde.serializer();

		Topology topology = new Topology();
		topology = topology.addSource(LATEST, "kinaction_source", stringDeserializer, stringDeserializer, "kinaction_source_topic");

		//topology = topology.addProcessor("kinactionTestProcessor", () -> new KinactionTestProcessor(), "kinaction_source");
		
		topology = topology.addSink("Kinaction-Destination1-Topic", "kinaction_destination1_topic", stringSerializer, stringSerializer, "kinactionTestProcessor");

		topology = topology.addSink("Kinaction-Destination2-Topic", "kinaction_destination2_topic", stringSerializer, stringSerializer, "kinactionTestProcessor");

		KafkaStreams kafkaStreams = new KafkaStreams(topology, kaProperties);
		kafkaStreams.cleanUp();
		kafkaStreams.start();
		Thread.sleep(5000);
		kafkaStreams.close();
	}

}
