# Commands used in Chapter 8

## Flume configuration for watching a directory

````
ag.sources = logdir  
ag.sinks = kafkasink
ag.channels = c1

#Configure the source directory to watch
ag.sources.logdir.type = spooldir 
ag.sources.logdir.spoolDir = /var/log/applogs
ag.sources.logdir.fileHeader = true

# Describe the Kafka sink 
ag.sinks.kafkasink.channel = c1  
ag.sinks.kafkasink.type = org.apache.flume.sink.kafka.KafkaSink
ag.sinks.kafkasink.kafka.topic = test-topic
ag.sinks.kafkasink.kafka.bootstrap.servers = kafka-1:9092, kafka-2:9092
ag.sinks.kafkasink.kafka.flumeBatchSize = 10
ag.sinks.kafkasink.kafka.producer.acks = 1
ag.sinks.kafkasink.kafka.producer.linger.ms = 5
ag.sinks.kafkasink.kafka.producer.compression.type = snappy

# Memory channel configuration 
ag.channels.c1.type = memory   
ag.channels.c1.capacity = 1000
ag.channels.c1.transactionCapacity = 100

# Bind both the sink and source to the same channel
ag.sources.logdir.channels = c1  ag.sinks.kafkasink.channel = c1


````


## Flume Kafka Channel configuration
````
ag.channels.channel1.type = org.apache.flume.channel.kafka.KafkaChannel 
ag.channels.channel1.kafka.bootstrap.servers = kafka1:9092,kafka2:9092,kafka3:9092 
ag.channels.channel1.kafka.topic = channel1-topic 
ag.channels.channel1.kafka.consumer.group.id = flume-cgroup 

````


## Secor

````
java -ea -Dlog4j.configuration=log4j.prod.properties \ 
  -Dconfig=secor.prod.backup.properties \ 
  -cp secor-0.1-SNAPSHOT.jar:lib/* \ 
  com.pinterest.secor.main.ConsumerMain
  
````