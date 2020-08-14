# Commands used in Chapter 9

## Creating the selfserviceTopic Topic

     bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic selfserviceTopic --partitions 2 --replication-factor 2 

You should see the ouput: `Created topic "selfserviceTopic".`

## Kafkacat Producer usage
 kafkacat -P -b localhost:9092 -t selfserviceTopic
 
## Kafkacat Consumer usage

kafkacat -C -b localhost:9092 -t selfserviceTopic

## REST Proxy startup

./bin/kafka-rest-start ./etc/kafka-rest/kafka-rest.properties 

## Curl call to REST Proxy for Topic list

 curl -X GET -H "Accept: application/vnd.kafka.v2+json" localhost:8082/topics 
 
##  Zookeeper Unit File

[Unit]
Requires=network.target remote-fs.target
After=network.target remote-fs.target

[Service]
Type=simple
ExecStart=/opt/kafka/bin/zookeeper-server-start.sh /opt/kafka/config/zookeeper.properties 
ExecStop=/opt/kafka/bin/zookeeper-server-stop.sh 
Restart=on-abnormal 

[Install]
WantedBy=multi-user.target

## Kafka Unit File

[Unit]
Requires=zookeeper.service
After=zookeeper.service 

[Service]
Type=simple
ExecStart=/opt/kafka/bin/kafka-server-start.sh \
/opt/kafka/bin/config/server.properties > /opt/kafka/broker.log 2>&1' 
ExecStop=/home/kafka/kafka/bin/kafka-server-stop.sh 
Restart=on-abnormal 

[Install]
WantedBy=multi-user.target

## Kafka Startup with Systemctl

sudo systemctl start zookeeper
sudo systemctl start kafka

## Kafka server log retention

log4j.appender.kafkaAppender.MaxFileSize=100KB 
log4j.appender.kafkaAppender.MaxBackupIndex=10

## Which Kafka Appender

log4j.appender.kafkaAppender=org.apache.kafka.log4jappender.KafkaLog4jAppender


<dependency>
	<groupId>org.apache.kafka</groupId>
	<artifactId>kafka-log4j-appender</artifactId>
	<version>2.2.1</version>
</dependency>

## Kafka JMX Options

KAFKA_JMX_OPTS="-Djava.rmi.server.hostname=127.0.0.1  
-Dcom.sun.management.jmxremote.local.only=false  
-Dcom.sun.management.jmxremote.rmi.port=1099  
-Dcom.sun.management.jmxremote.authenticate=false  
-Dcom.sun.management.jmxremote.ssl=false"

## Kafka Consumer Group Command to find lag usage

bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 \
--describe --group test-consumer

