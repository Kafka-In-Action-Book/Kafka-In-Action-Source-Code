# Commands used in Chapter 2

## Creating the helloworld Topic

    bin/kafka-topics.sh --zookeeper localhost:2181 \
    --create --topic helloworld --partitions 3 --replication-factor 3

You should see the ouput: `Created topic helloworld.`

## Verify the Topic

    bin/kafka-topics.sh --zookeeper localhost:2181 --list

    
## Kafka Producer Console Command

    bin/kafka-console-producer.sh --broker-list localhost:9092 --topic helloworld
    
## Kafka Consumer Console Command

    bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 \
    --topic helloworld --from-beginning
    
## Java Client POM entry

        <dependency>
            <groupId>org.apache.kafka</groupId>
            <artifactId>kafka-clients</artifactId>
            <version>2.4.0</version>
        </dependency>