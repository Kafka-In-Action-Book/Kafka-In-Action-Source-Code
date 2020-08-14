package com.kafkainaction.kstreams;

import java.util.Properties;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;

public class PriceTrackerKStreamProcessor {

	public static void main(String[] args) throws Exception {

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "PriceTracker");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        
    	StreamsBuilder builder = new StreamsBuilder();

    	KStream<String, Price> priceStream = builder.stream("price"); 
    	priceStream.print(Printed.<String, Price>toSysOut().withLabel( "Price Tracker")); 

    	KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), props);
    	kafkaStreams.cleanUp(); 
    	kafkaStreams.start();
    	Thread.sleep(5000);
    	kafkaStreams.close(); 
    }
}
