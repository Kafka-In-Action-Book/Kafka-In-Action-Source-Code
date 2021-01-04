package com.kafkainaction.kstreams2;

import com.kafkainaction.kstreams2.SchemaSerdes;
import com.kafkainaction.kstreams2.TransactionTransformer;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.processor.MockProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.junit.Before;
import org.junit.Test;
import org.kafkainaction.Funds;
import org.kafkainaction.Transaction;
import org.kafkainaction.TransactionResult;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.kafkainaction.ErrorType.INSUFFICIENT_FUNDS;
import static org.kafkainaction.TransactionType.DEPOSIT;
import static org.kafkainaction.TransactionType.WITHDRAW;


public class TransactionTransformerTest {
  
  private KeyValueStore<String, Funds> fundsStore;
  private MockProcessorContext mockContext;
  private TransactionTransformer transactionTransformer;
  final static Map<String, String> testConfig = Map.of(
      BOOTSTRAP_SERVERS_CONFIG, "localhost:8080",
      APPLICATION_ID_CONFIG, "mytest",
      SCHEMA_REGISTRY_URL_CONFIG, "mock://schema-registry.kafkainaction.org:8080"
  );


  @Before
  public void setup() {
    final Properties properties = new Properties();
    properties.putAll(testConfig);
    mockContext = new MockProcessorContext(properties);

    final SpecificAvroSerde<Funds>
        specificAvroSerde =
        SchemaSerdes.getSpecificAvroSerde(properties);
    
    fundsStore = Stores.keyValueStoreBuilder(
        Stores.inMemoryKeyValueStore("fundsStore"),
        Serdes.String(),
        specificAvroSerde)
        .withLoggingDisabled()    // Changelog is not supported by MockProcessorContext.
        .build();

    fundsStore.init(mockContext, fundsStore);
    mockContext.register(fundsStore, null);

    transactionTransformer = new TransactionTransformer(fundsStore.name());
    transactionTransformer.init(mockContext);
  }

  @Test
  public void shouldStoreTransaction() {
    final Transaction
        transaction =
        new Transaction(UUID.randomUUID().toString(), "1", new BigDecimal(100), DEPOSIT, "USD", "USA");
    final TransactionResult transactionResult = transactionTransformer.transform(transaction);

    assertThat(transactionResult.getSuccess()).isTrue();

  }

  @Test
  public void shouldHaveInsufficientFunds() {
    final Transaction
        transaction =
        new Transaction(UUID.randomUUID().toString(),
                        "1",
                        new BigDecimal("100"),
                        WITHDRAW, "RUR",
                        "Russia");
    final TransactionResult result = transactionTransformer.transform(transaction);

    assertThat(result.getSuccess()).isFalse();
    assertThat(result.getErrorType()).isEqualTo(INSUFFICIENT_FUNDS);
  }

  @Test
  public void shouldHaveEnoughFunds() {
    final Transaction transaction1 =
        new Transaction(UUID.randomUUID().toString(), "1", new BigDecimal("300"), DEPOSIT, "RUR",
                        "Russia");

    final Transaction transaction2 =
        new Transaction(UUID.randomUUID().toString(), "1", new BigDecimal("200"), WITHDRAW, "RUR",
                        "Russia");
    transactionTransformer.transform(transaction1);
    final TransactionResult result = transactionTransformer.transform(transaction2);

    assertThat(result.getSuccess()).isTrue();
    assertThat(result.getErrorType()).isNull();
  }
}