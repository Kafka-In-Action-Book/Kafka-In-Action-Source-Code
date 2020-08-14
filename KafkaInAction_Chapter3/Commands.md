# Source Code for Kafka in Action

## Chapter 3 Specific Notes
* The commands and scripts are meant to be ran from your Kafka root directory: ie. you should see the folder bin, config, etc when you do an `ls` in this directory.

* If you get a port in use exception: kill the process related to that port. For example, on a Mac, you can do the following command: `lsof -t -i tcp:8083 | xargs kill` if the connect-standalone process is still running, etc.


## sqlite3 database example
* Close all zookeeper and kafka brokers you have open. We are going to start fresh.

### Download for Confluent Open Source
* https://www.confluent.io/download/
Select the Confluent Open Source version 5.4.0 or above and unzip that archive.

### Table create command in the root folder you just downloaded:
* sqlite3 kafkatest.db
 
 CREATE TABLE invoices( 
   id INT PRIMARY KEY     NOT NULL,
   title           TEXT    NOT NULL,
   details        CHAR(50),
   billedamt         REAL,
   modified    DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL
);

### Table insert command

	INSERT INTO invoices (id,title,details,billedamt)  VALUES (1, 'book', 'Franz Kafka', 500.00 );

### Edit source jdbc connector file to the following:

	vi etc/kafka-connect-jdbc/source-quickstart-sqlite.properties 
	----
	name=test-source-sqlite-jdbc-invoice
	connector.class=io.confluent.connect.jdbc.JdbcSourceConnector
	tasks.max=1
	# SQLite database stored in the file kafkatest.db, use and auto-incrementing column called 'id' to
	# detect new rows as they are added, and output to topics prefixed with 'test-sqlite-jdbc-', e.g.
	# a table called 'users' will be written to the topic 'test-sqlite-jdbc-users'.
	connection.url=jdbc:sqlite:kafkatest.db
	mode=incrementing
	incrementing.column.name=id
	topic.prefix=test-sqlite-jdbc-
	----

### Start Confluent Kafka
Run the following:
* bin/confluent start
* bin/confluent load jdbc-source
* bin/confluent stop connect
* bin/confluent start connect
* ./bin/kafka-avro-console-consumer --topic test-sqlite-jdbc-invoices --zookeeper localhost:2181  --from-beginning


## Avro Notes
* You need to be running a schema registry to make this work.
Make sure you start the ./bin/confluent start before you try and run the example.
* You should see the producer message by running:
 ./bin/kafka-avro-console-consumer --topic avrotest --zookeeper localhost:2181  --from-beginning
