package com.kafkainaction.kstreams2;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.kafkainaction.Funds;
import org.kafkainaction.Transaction;
import org.kafkainaction.TransactionResult;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;

import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG;

public class TransactionProcessor {

  private final String transactionsInputTopicName;
  private final String transactionSuccessTopicName;
  private final String transactionFailedTopicName;
  private final String fundsStoreName;

  public TransactionProcessor(final String transactionsInputTopicName,
                              final String transactionSuccessTopicName,
                              final String transactionFailedTopicName,
                              final String fundsStoreName) {

    this.transactionsInputTopicName = transactionsInputTopicName;
    this.transactionSuccessTopicName = transactionSuccessTopicName;
    this.transactionFailedTopicName = transactionFailedTopicName;
    this.fundsStoreName = fundsStoreName;
  }

  private static boolean success(String account, TransactionResult result) {
    return result.getSuccess();
  }

  public static void main(String[] args) {
    String transactionsInputTopicName = "transaction-request";
    String transactionSuccessTopicName = "transaction-success";
    String transactionFailedTopicName = "transaction-failed";
    String fundsStoreName = "funds-store";

    final TransactionProcessor transactionProcessor = new TransactionProcessor(transactionsInputTopicName,
                                                                               transactionSuccessTopicName,
                                                                               transactionFailedTopicName,
                                                                               fundsStoreName);
    Properties props = new Properties();
    props.put(APPLICATION_ID_CONFIG, "transaction-processor");
    props.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

    transactionProcessor
        .createTopics(props, transactionsInputTopicName, transactionSuccessTopicName, transactionFailedTopicName);

    StreamsBuilder builder = new StreamsBuilder();
    final SpecificAvroSerde<Transaction> transactionRequestAvroSerde = SchemaSerdes.getSpecificAvroSerde(props);
    final SpecificAvroSerde<TransactionResult> transactionResultAvroSerde = SchemaSerdes.getSpecificAvroSerde(props);
    final SpecificAvroSerde<Funds> fundsSerde = SchemaSerdes.getSpecificAvroSerde(props);

    final Topology topology = transactionProcessor.topology(builder,
                                                            transactionRequestAvroSerde,
                                                            transactionResultAvroSerde,
                                                            fundsSerde);

    final KafkaStreams streams = new KafkaStreams(topology, props);
    final CountDownLatch latch = new CountDownLatch(1);

    // Attach shutdown handler to catch Control-C.
    Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
      @Override
      public void run() {
        streams.close(Duration.ofSeconds(5));
        latch.countDown();
      }
    });

    try {
      streams.start();
      latch.await();
    } catch (Throwable e) {
      System.exit(1);
    }
    System.exit(0);
  }

  private void createTopics(Properties p, String... names) {
    try (AdminClient client = AdminClient.create(p)) {

      final List<NewTopic> topicList = Arrays.stream(names)
          .map(name -> new NewTopic(name, 6, (short) 1))
          .collect(Collectors.toList());
      client.createTopics(topicList);
    }
  }

  public Topology topology(final StreamsBuilder builder,
                           SpecificAvroSerde<Transaction> transactionRequestAvroSerde,
                           SpecificAvroSerde<TransactionResult> transactionResultAvroSerde,
                           SpecificAvroSerde<Funds> fundsSerde) {

    final StoreBuilder<KeyValueStore<String, Funds>>
        stateStoreBuilder =
        Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(this.fundsStoreName),
                                    Serdes.String(), fundsSerde);
    builder.addStateStore(stateStoreBuilder);

    KStream<String, Transaction> transactionStream = builder.stream(this.transactionsInputTopicName,
                                                                    Consumed.with(Serdes.String(),
                                                                                  transactionRequestAvroSerde));
    KStream<String, TransactionResult>
        resultStream =
        transactionStream.transformValues(() -> new TransactionTransformer(this.fundsStoreName), this.fundsStoreName);

    resultStream
        .filter(TransactionProcessor::success)
        .to(this.transactionSuccessTopicName,
            Produced.with(Serdes.String(), transactionResultAvroSerde));

    resultStream
        .filterNot(TransactionProcessor::success)
        .to(this.transactionFailedTopicName,
            Produced.with(Serdes.String(), transactionResultAvroSerde));

    return builder.build();
  }


}
