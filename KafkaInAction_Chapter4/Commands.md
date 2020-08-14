# Commands used in Chapter 4

## Creating the alert Topic

bin/kafka-topics.sh --zookeeper localhost:2181     --create --topic alert --partitions 3 --replication-factor 3

## Verify the Custom partitioner sent the alert to the correct partition
bin/kafka-run-class.sh kafka.tools.GetOffsetShell --broker-list localhost:9092 --topic alert --time -1
