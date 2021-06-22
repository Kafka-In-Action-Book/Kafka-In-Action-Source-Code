# Commands used in Chapter 7

## Listing cluster topics

	bin/kafka-topics.sh --bootstrap-server localhost:9092 --list


## Creating a topic

	bin/kafka-topics.sh --bootstrap-server localhost:9092 --create \ 
	--topic my_test_topic \ 
	--replication-factor 2 \ 
	--partitions 2

    
## Adding more partitions to an existing topic

	kafka-topics.sh --bootstrap-server localhost:9092 --alter \
	--topic my_test_topic --partitions 3 
    
## Deleting a topic

	kafka-topics.sh --bootstrap-server localhost:9092 --delete \ // #A <1>
	--topic my_test_topic
	
	
## Looking at a dump of a segment

	bin/kafka-dump-log.sh --print-data-log \   #<1>
         --files /tmp/kafka-logs-0/test-1/00000000000000000000.log

	
## Electing Preferred Leader

	bin/kafka-leader-election.sh --bootstrap-server localhost:9092    \        
          --election-type preferred --partition 1 \ #<1>
          --topic test # <2>
 
	
## Example Preferred Leader Json

	 {"partitions":                        
    	   [{"topic": "test", "partition": 1},   // #A <1>
        	{"topic": "my_test", "partition": 2}
       	  ]
	} 
	
## Creating a compacted topic

	kafka-topics.sh --bootstrap-server localhost:9092 --create \
	--topic compact_test_topic --replication-factor 2 --partitions 2 \
	--config cleanup.policy=compact 

