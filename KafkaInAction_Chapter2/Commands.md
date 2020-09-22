# Commands used in Chapter 2

## Creating the helloworld Topic

```shell script
kafka-topics.sh --bootstrap-server localhost:909 \
 --create --topic helloworld --partitions 3 --replication-factor 3
```

You should see the output: `Created topic helloworld.`

## Verify the Topic

```shell script
kafka-topics.sh --bootstrap-server localhost:9092 --list
```

## Kafka Producer Console Command

```shell script
kafka-console-producer.sh --bootstrap-server localhost:9092 --topic helloworld
```
    
## Kafka Consumer Console Command

```shell script
kafka-console-consumer.sh --bootstrap-server localhost:9092 \
--topic helloworld --from-beginning
```
    
## Java Client POM entry

```xml
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-clients</artifactId>
    <version>2.6.0</version>
</dependency>
```