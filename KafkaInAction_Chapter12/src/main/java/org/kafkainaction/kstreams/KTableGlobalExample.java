package org.kafkainaction.kstreams;

import java.util.Properties;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;

public class KTableGlobalExample {
	
	private EmailService emailService = new EmailService();

	public static void main(String[] args) throws Exception {

        Properties kaProperties = new Properties();
        kaProperties.put(StreamsConfig.APPLICATION_ID_CONFIG, "Kinaction_GlobalKTableExample");
        kaProperties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094");
        
    	StreamsBuilder builder = new StreamsBuilder();

    	final KStream<String, MailingNotif> lists = builder.stream("kinaction_mailingNotif");
    	final GlobalKTable<String, Customer> customers = builder.globalTable("kinaction_custinfo");

    	//uncomment to work on table logic if desired
 - 
//		lists.join(customers, (mailingNotifID, mailingNotif) -> mailingNotif.getCustomerId(),
//				(mailingNotif, customer) -> new Email(mailingNotif, customer))
//				.peek((key, email) -> emailService.sendMessage(email));
//        

        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), kaProperties);
    	kafkaStreams.cleanUp();
    	kafkaStreams.start();
    	Thread.sleep(5000);
    	kafkaStreams.close(); 
    }
}
