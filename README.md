# Source Code for Kafka in Action

## Most up-to-date location
* While the source code might be included as a zip file from the Manning website, the location that will likely be the most up-to-date will be located at https://github.com/Kafka-In-Action-Book/Kafka-In-Action-Source-Code. The authors recommend referring to that site rather than the Manning zip location if you have a choice.


### Errata

* If you happen to find errata, one option is to look at: https://github.com/Kafka-In-Action-Book/Kafka-In-Action-Source-Code/blob/master/errata.md
* This is not the official Manning site errata list for the book - but please feel free to create a pull request to share any errata that you wish to share to help others.

## Security Concerns
* Please check out the following to stay up-to-date on any security issues. The code in this project is NOT production ready and dependencies might have VULNERABILITIES that evolve over time.
* https://kafka.apache.org/cve-list


## Notes

Here are some notes regarding the source code:

1. Select shell commands will be presented in a [Markdown](https://daringfireball.net/projects/markdown/syntax) format in a file called Commands.md or in [AsciiDoc](https://docs.asciidoctor.org/asciidoc/latest/) called Commands.adoc for each Chapter folder if there are any commands selected in that chapter.
2. Not all commands and snippets of code are included here that were in the book material. As a beginner book, some sections were meant only to give an idea of the general process and not complete examples.

### Requirements
This project was built with the following versions:

1. Java 11 
2. Apache Maven 3.6.x.
We provide [Maven Wrapper](https://github.com/takari/maven-wrapper), so you don't need to install Maven yourself.

### How to build

Run following command in the root of this project to build all examples:

    ./mvnw verify 

Run following command in the root of this project to build specific example.
For example, to build only example from Chapter 12 run:

    ./mvnw --projects KafkaInAction_Chapter12 verify

### IDE setup
 
1. We have used Eclipse for our IDE. To set up for eclipse run mvn eclipse:eclipse from the base directory of this repo. Or, you can Import->Existing Maven Projects.


### Installing Kafka
Run the following in a directory (without spaces in the path) once you get the artifact downloaded. Refer to Appendix A if needed.

    tar -xzf kafka_2.13-2.7.1.tgz
    cd kafka_2.13-2.7.1

### Running Kafka
1. To start Kafka go to <install dir>/kafka_2.13-2.7.1/
2. Run bin/zookeeper-server-start.sh config/zookeeper.properties
3. Modify the Kafka server configs

	````
	cp config/server.properties config/server0.properties
	cp config/server.properties config/server1.properties
	cp config/server.properties config/server2.properties
	````
	
	vi config/server0.properties
	````
	broker.id=0
	listeners=PLAINTEXT://localhost:9092
	log.dirs=/tmp/kafkainaction/kafka-logs-0
	````
	
	vi config/server1.properties
	
	````
	broker.id=1
	listeners=PLAINTEXT://localhost:9093
	log.dirs=/tmp/kafkainaction/kafka-logs-1
	````
	
	vi config/server2.properties
	
	````
	broker.id=2
	listeners=PLAINTEXT://localhost:9094
	log.dirs=/tmp/kafkainaction/kafka-logs-2
	````
	
4. Start the Kafka Brokers:
    
````	
    bin/kafka-server-start.sh config/server0.properties
    bin/kafka-server-start.sh config/server1.properties
    bin/kafka-server-start.sh config/server2.properties
````	
 
### Stopping Kafka

1. To stop Kafka go to the Kafka directory install location
1. Run bin/kafka-server-stop.sh
1. Run bin/zookeeper-server-stop.sh

### Code by Chapter
Most of the code from the book can be found in the project corresponding to the chapter. Some code has been moved to other chapters in order to reduce the number of replication of related classes.
 
### Running the examples
 
Most of the example programs can be run from within an IDE or from the command line. Make sure that your ZooKeeper and Kafka Brokers are up and running before you can run any of the examples.

The examples will usually write out to topics and print to the console.

### Shell Scripts

In the Chapter 2 project, we have included a couple of scripts if you want to use them under `src/main/resources`.

They include:
* `starteverything.sh` //This will start your ZooKeeper and Kafka Brokers (you will still have to go through the first time setup with Appendix A before using this.)
* stopeverything.sh // Will stop ZooKeeper and your brokers
* portInUse.sh // If you get a port in use error on startup, this script will kill all of the processes using those ports (assuming you are using the same ports as in Appendix A setup).
	
## Disclaimer

The author and publisher have made every effort to ensure that the information in this book was correct at press time. The author and publisher do not assume and hereby disclaim any
liability to any party for any loss, damage, or disruption caused by errors or omissions, whether
such errors or omissions result from negligence, accident, or any other cause, or from any usage
of the information herein. Note: The information in this book also refers to and includes the source code found here.	

