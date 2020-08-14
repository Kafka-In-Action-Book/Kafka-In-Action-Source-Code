package com.kafkainaction.kstreams;

import java.util.Properties;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;

public class KTableGlobalExample {
	
	private EmailService emailService = new EmailService();

	public static void main(String[] args) throws Exception {

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "GlobalKTableExample");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        
    	StreamsBuilder builder = new StreamsBuilder();

    	final KStream<String, MailingNotif> lists = builder.stream("mailingNotif");
    	final GlobalKTable<String, Customer> customers = builder.globalTable("customers");

    	//TODO - 
//		lists.join(customers, (mailingNotifID, mailingNotif) -> mailingNotif.getCustomerId(),
//				(mailingNotif, customer) -> new Email(mailingNotif, customer))
//				.peek((key, email) -> emailService.sendMessage(email));
//        

        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), props);
    	kafkaStreams.cleanUp();
    	kafkaStreams.start();
    	Thread.sleep(5000);
    	kafkaStreams.close(); 
    }
}
