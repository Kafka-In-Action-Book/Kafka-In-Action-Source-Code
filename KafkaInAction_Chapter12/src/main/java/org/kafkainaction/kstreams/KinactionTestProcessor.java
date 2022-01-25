package org.kafkainaction.kstreams;

import org.apache.kafka.streams.processor.To;
import org.apache.kafka.streams.processor.api.ContextualProcessor;
import org.apache.kafka.streams.processor.api.Record;

public class KinactionTestProcessor extends ContextualProcessor<String, String, String, String> {

  @Override
  public void process(final Record<String, String> record) {
    context().forward(record, String.valueOf(To.child("Kinaction-Destination2-Topic")));
  }
}
