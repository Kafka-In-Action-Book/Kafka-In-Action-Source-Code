# Commands used in Chapter 7

## Listing cluster topics

	bin/kafka-topics.sh --bootstrap-server localhost:9092 --list


## Creating a topic

	bin/kafka-topics.sh --bootstrap-server localhost:9092 --create \ 
	--topic kinaction_topic \ 
	--replication-factor 2 \ 
	--partitions 2

    
## Adding more partitions to an existing topic

	kafka-topics.sh --bootstrap-server localhost:9092 --alter \
	--topic kinaction_topic --partitions 3 
    
## Deleting a topic

	kafka-topics.sh --bootstrap-server localhost:9092 --delete \ // #A <1>
	--topic kinaction_topic
	
	
## Looking at a dump of a segment

	bin/kafka-dump-log.sh --print-data-log \   #<1>
         --files /tmp/kafka-logs-0/kinaction_topic-1/00000000000000000000.log

	
## Electing Preferred Leader

	bin/kafka-leader-election.sh --bootstrap-server localhost:9092    \        
          --election-type preferred --partition 1 \ #<1>
          --topic kinaction_test # <2>
 
	
## Example Preferred Leader Json

	 {"partitions":                        
    	   [{"topic": "kinaction_test", "partition": 1},   // #A <1>
        	{"topic": "kinaction_test_1", "partition": 2}
       	  ]
	} 
	
## Creating a compacted topic

	kafka-topics.sh --bootstrap-server localhost:9092 --create \
	--topic kinaction_compact --replication-factor 3 --partitions 3 \
	--config cleanup.policy=compact 

