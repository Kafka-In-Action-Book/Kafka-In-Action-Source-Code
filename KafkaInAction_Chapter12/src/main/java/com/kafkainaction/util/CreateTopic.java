package com.kafkainaction.util;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.errors.TopicExistsException;

public class CreateTopic {
	
	public static void main(String[] args) {
		
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092,localhost:9093");
		props.put("key.serializer", "com.kafkainaction.serde.AlertKeySerde");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		 NewTopic requestedTopic = new NewTopic("selfserviceTopic", 2,(short) 2); 
		 AdminClient client = AdminClient.create(props);
		 CreateTopicsResult topicResult = client.createTopics(Collections.singleton(requestedTopic));
		 try {
			topicResult.values().get("selfserviceTopic").get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
            if (!(e.getCause() instanceof TopicExistsException)) {
                throw new RuntimeException(e.getMessage(), e);
            }
			e.printStackTrace();
		}

	}


}
