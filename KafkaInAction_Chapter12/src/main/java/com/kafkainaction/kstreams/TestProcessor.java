package com.kafkainaction.kstreams;

import org.apache.kafka.streams.processor.AbstractProcessor;
import org.apache.kafka.streams.processor.To;

public class TestProcessor extends AbstractProcessor<String, String> {

    @Override
    public void process(String key, String value) {
      context().forward(key, value, To.child("Output-Sink2"));
    }

}
