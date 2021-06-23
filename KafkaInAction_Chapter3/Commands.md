# Source Code for Kafka in Action

## Chapter 3 Specific Notes

* The commands and scripts are meant to be run from your Kafka root directory: i.e. you should see the folder `bin`, `config`, etc when you do an `ls` in this directory.

* If you get a port in use exception: kill the process related to that port. 
For example, on a Mac, you can do the following command: `lsof -t -i tcp:8083 | xargs kill` if the `connect-standalone` process is still running, etc.

## sqlite3 database example

* Close all zookeeper and kafka brokers you have open. 
We are going to start fresh.

### Download for Confluent Open Source

* https://www.confluent.io/previous-versions
  Select the «Download Enterprise Tarball» version 6.1.1 or above and unzip that archive.
  Enterprise tarball is free for single node installation https://docs.confluent.io/platform/current/installation/license.html#developer-license, and includes JDBC connector jars.
  
* install Confluent CLI https://docs.confluent.io/current/cli/installing.html

### Table create command in the root folder you just downloaded:

* `sqlite3 kafkatest.db`

```sqlite
 CREATE TABLE invoices( 
   id INT PRIMARY KEY     NOT NULL,
   title           TEXT    NOT NULL,
   details        CHAR(50),
   billedamt         REAL,
   modified    DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL
);
``` 

### Table insert command

```sqlite
INSERT INTO invoices (id,title,details,billedamt)  VALUES (1, 'book', 'Franz Kafka', 500.00 );
```

### Edit source jdbc connector file to the following:

`> cp $CONFLUENT_HOME/etc/kafka-connect-jdbc/source-quickstart-sqlite.properties $CONFLUENT_HOME/etc/kafka-connect-jdbc/kafkatest-sqlite.properties`
`> vi $CONFLUENT_HOME/etc/kafka-connect-jdbc/kafkatest-sqlite.properties`
 
```properties
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
```

### Start Confluent Kafka

Run the following:

```bash
> confluent local services connect start 
> confluent local services connect connector load jdbc-source
> confluent local services connect connector status
> ./bin/kafka-avro-console-consumer --topic test-sqlite-jdbc-invoices --bootstrap-server localhost:9092  --from-beginning
```

## Avro Notes

* You need to be running a schema registry to make this work.
Make sure you start the `confluent local services connect start` before you try, and run the example.

* You should see the producer message by running:

```bash
confluent local services kafka consume avrotest --value-format avro --from-beginning
```

## Java example commands

NOTE:
 * Use JDK 11
 * Apache Kafka and Confluent Schema Registry should be up and running

```bash
> ./mvnw verify # build a uber jar
> java -cp target/chapter3-jar-with-dependencies.jar org.kafkainaction.consumer.HelloWorldConsumer # run a consumer application
> java -cp target/chapter3-jar-with-dependencies.jar org.kafkainaction.producer.HelloWorldProducer # run a producer

``` 
