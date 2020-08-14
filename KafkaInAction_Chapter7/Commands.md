# Commands used in Chapter 7

## Listing cluster topics

	kafka-topics.sh --zookeeper localhost:2181 --list


## Creating a topic

	kafka-topics.sh --zookeeper localhost:2181 --create \ 
	--topic my_test_topic \ 
	--replication-factor 2 \ 
	--partitions 2

    
## Adding more partitions to an existing topic

	kafka-topics.sh --zookeeper localhost:2181 --alter \
	--topic my_test_topic --partitions 3 
    
## Deleting a topic

	kafka-topics.sh --zookeeper localhost:2181 --delete \ // #A <1>
	--topic my_test_topic
	
	
## Looking at a dump of a segment

	kafka-run-class.sh kafka.tools.DumpLogSegments --print-data-log \ 
	--files logs/test-0/00000000000000000000.log 
	
## Alter command to edit configuration 

	kafka-configs.sh --zookeeper localhost:2181 --alter \ 
	--entity-type topics 
	--entity-name test  
	--add-config segment.ms=60000
	
## Delete altered config

	kafka-configs.sh --zookeeper localhost:2181 --alter \ 
	--entity-type topics --entity-name test \
	--delete-config segment.ms
	
## Topics File for Reassigment Content

	{"topics":                            
  	[{"topic": "test"}], 
  	"version":1 
	}


## Generate a reassign plan

	kafka-reassign-partitions.sh --zookeeper localhost:2181 --generate \ 
	--topics-to-move-json-file topics.json \ 
	--broker-list 0,1,2 
	
## Executing our plan

	kafka-reassign-partitions.sh --zookeeper localhost:2181 --execute \ 
	--reassignment-json-file plan.json
	
## Verify our execution

	kafka-reassign-partitions.sh --zookeeper localhost:2181 --verify \ 
	--reassignment-json-file plan.json
	
## Create a Plan Adding a Replica

	{"partitions":                            
  	[{"topic": "test", "partition": 0,  
    	"replicas": [  // #B <2>
     	0,  
     	1
    	]
   	}
  	],
  	"version":1 
	}
	
## Executing our plan to increase replica count

	kafka-reassign-partitions.sh --zookeeper localhost:2181 --execute \ 
	--reassignment-json-file replicacount.json
	
## Electing Preferred Leader

	kafka-preferred-replica-election.sh --zookeeper localhost:2181 
	
## Example Preferred Leader Json

	 {"partitions":                        
    	   [{"topic": "test", "partition": 1},   // #A <1>
        	{"topic": "my_test", "partition": 2}
       	]
	} 
	
## Creating a compacted topic

	kafka-topics.sh --zookeeper localhost:2181 --create \
	--topic compact_test_topic --replication-factor 2 --partitions 2 \
	--config cleanup.policy=compact 

