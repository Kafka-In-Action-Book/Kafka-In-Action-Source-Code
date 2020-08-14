# Commands used in Chapter 6

## Listing topics from Zookeeper

	zookeeper-shell.sh localhost:2181 
	ls /brokers/topics

## Find Controller in ZooKeeper

    zookeeper-shell.sh localhost:2181 
	get /controller 

    
## Starting a broker with a JMX Port

	JMX_PORT=9990 bin/kafka-server-start.sh config/server0.properties 
	
## Mirror Maker Command

    bin/kafka-mirror-maker.sh 
      --consumer.config source.properties 
      --producer.config target.properties --whitelist test-topic
    
## Describe Topic

    $ ./kafka-topics.sh --describe --zookeeper localhost:2181 --topic replica-test 
    
    //Sample output
	Topic:replica-test	PartitionCount:1	ReplicationFactor:3	Configs:
	Topic: replica-test	Partition: 0	Leader: 0	Replicas: 1,0,2	Isr: 0,2
	
	
## Under-replicated-partitions flag

	./kafka-topics.sh --zookeeper localhost:2181 --describe --under-replicated-partitions 

	//Sample output
	Topic: replica-test	Partition: 0	Leader: 0	Replicas: 1,0,2	Isr: 0,2