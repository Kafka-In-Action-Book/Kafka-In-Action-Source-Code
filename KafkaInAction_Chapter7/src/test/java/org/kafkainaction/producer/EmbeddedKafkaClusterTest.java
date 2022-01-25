package org.kafkainaction.producer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.integration.utils.EmbeddedKafkaCluster;
import org.apache.kafka.test.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.kafkainaction.consumer.AlertConsumer;
import org.kafkainaction.model.Alert;
import org.kafkainaction.serde.AlertKeySerde;

import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class EmbeddedKafkaClusterTest {

  private static final String TOPIC = "kinaction_alert";
  private static final int PARTITION_NUMER = 3;
  private static final int REPLICATION_NUMBER = 3;
  private static final int BROKER_NUMBER = 3;

  //@ClassRule
  public static EmbeddedKafkaCluster embeddedKafkaCluster;

  private Properties kaProducerProperties;
  private Properties kaConsumerProperties;

  @Before
  public void setUpBeforeClass() throws Exception {
    embeddedKafkaCluster = new EmbeddedKafkaCluster(BROKER_NUMBER);
    embeddedKafkaCluster.start();
    embeddedKafkaCluster.createTopic(TOPIC, PARTITION_NUMER, REPLICATION_NUMBER);
    kaProducerProperties = TestUtils.producerConfig(embeddedKafkaCluster.bootstrapServers(),
                                                    AlertKeySerde.class,
                                                    StringSerializer.class);

    kaConsumerProperties = TestUtils.consumerConfig(embeddedKafkaCluster.bootstrapServers(),
                                                    AlertKeySerde.class,
                                                    StringDeserializer.class);
  }

  @Test
  public void testAlertPartitioner() throws InterruptedException {
    AlertProducer alertProducer = new AlertProducer();
    try {
      alertProducer.sendMessage(kaProducerProperties);
    } catch (Exception ex) {
      fail("kinaction_error EmbeddedKafkaCluster exception" + ex.getMessage());
    }

    AlertConsumer alertConsumer = new AlertConsumer();
    ConsumerRecords<Alert, String> records = alertConsumer.getAlertMessages(kaConsumerProperties);
    TopicPartition partition = new TopicPartition(TOPIC, 0);
    List<ConsumerRecord<Alert, String>> results = records.records(partition);
    assertEquals(0, results.get(0).partition());
  }

}
