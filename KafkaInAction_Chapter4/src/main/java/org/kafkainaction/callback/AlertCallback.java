package org.kafkainaction.callback;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlertCallback implements Callback {    //<1>

  private static final Logger log = LoggerFactory.getLogger(AlertCallback.class);

  public void onCompletion(RecordMetadata metadata, Exception exception) {    //<2>
    if (exception != null) {
      log.error("Error sending message:", exception);
    } else {
      log.info("Message sent: offset = {}, topic = {}, timestamp = {}",
               metadata.offset(), metadata.topic(), metadata.timestamp());
    }
  }
}
