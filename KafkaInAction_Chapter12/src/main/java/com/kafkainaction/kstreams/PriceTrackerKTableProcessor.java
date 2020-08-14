package com.kafkainaction.kstreams;

import java.util.Properties;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Printed;

public class PriceTrackerKTableProcessor {

	public static void main(String[] args) throws Exception {

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "PriceTracker");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        
    	StreamsBuilder builder = new StreamsBuilder();
    	
    	KTable<String, Price> priceTable = builder.table("price"); 
    	priceTable.toStream().print(Printed.<String, Price>toSysOut().withLabel("Price KTable")); 
    	KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), props);
    	kafkaStreams.cleanUp();
    	kafkaStreams.start();
    	Thread.sleep(5000);
    	kafkaStreams.close(); 
    }
}
