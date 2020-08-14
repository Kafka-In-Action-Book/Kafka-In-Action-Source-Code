# Commands used in Chapter 11

## Important Schema Registry Configuration

listeners=http://0.0.0.0:8081
kafkastore.connection.url=localhost:2181 
kafkastore.topic=_schemas 
debug=false

## Starting Schema Registry

bin/schema-registry-start \\ 
  ./etc/schema-registry/schema-registry.properties
  
## Listing Schema Registry Configuration

curl -X GET http://localhost:8081/config

## Checking Compatibility with the Schema Registry REST API

curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \ 
  --data '{"schema": "{\"type\": \"string\"}"}' \ 
  http://localhost:8081/compatibility/subjects/topics-value/versions/latest 
  
## Checking Compatibility with the Schema Registry Maven Plugin

<plugin>
    <groupId>io.confluent</groupId>
    <artifactId>kafka-schema-registry-maven-plugin</artifactId> 
    <version>5.3.1</version>
    <configuration>
        <schemaRegistryUrls>
            <param>http://localhost:8081</param> 
        </schemaRegistryUrls>
        <subjects> 
            <Topics-key>src/main/avro/Topics-Key.avsc</Topics-key>
            <Topics-value>src/main/avro/Topics-Value.avsc</Topics-value>
        </subjects>
    </configuration>
    <goals>
        <goal>test-compatibility</goal> 
    </goals>
</plugin>