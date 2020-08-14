# Commands used in Chapter 10

## SSL Key Generation for a Broker

keytool -genkey -noprompt \
    -alias localhost \
    -dname "CN=kia.manning.com, OU=TEST, O=MASQUERADE, L=Portland, S=Or, C=US" \
    -keystore kafka.broker0.keystore.jks \ 
    -keyalg RSA \
    -storepass masquerade \ 
    -keypass masquerade \
    -validity 365

## Creating our own Certificate Authority

openssl req -new -x509 -keyout ca.key -out ca.crt \ 
  -days 999 -subj '/CN=localhost/OU=TEST/O=MASQUERADE/L=Portland/S=Or/C=US' \
  -passin pass:masquerade -passout pass:masquerade 
  
## Importing the CA cert into our truststores and signing our keystore

keytool -keystore kafka.client.truststore.jks \ 
-alias CARoot -import -file ca.crt  -keypass masquerade -storepass masquerade

keytool -keystore kafka.server.truststore.jks \ 
-alias CARoot -import -file ca.crt  -keypass masquerade -storepass masquerade

keytool -keystore kafka.broker0.keystore.jks \ 
-alias localhost -certreq -file cert-file -storepass masquerade -noprompt

openssl x509 -req -CA ca.crt -CAkey ca.key \ 
-in cert-file -out cert-signed -days 365 \
-CAcreateserial -passin pass:masquerade 

keytool -keystore kafka.broker0.keystore.jks \ 
-alias CARoot -import -file ca.crt -storepass masquerade -noprompt

keytool -keystore kafka.broker0.keystore.jks \ 
-alias localhost -import -file cert-signed -storepass masquerade -noprompt

## Broker server properties changes

listeners=PLAINTEXT://localhost:9092,SSL://localhost:9093 
ssl.truststore.location=/var/ssl/private/kafka.server.truststore.jks 
ssl.truststore.password=masquerade
ssl.keystore.location=/var/ssl/private/kafka.broker0.keystore.jks 
ssl.keystore.password=masquerade
ssl.key.password=masquerade

## SSL Configuration for Clients

security.protocol=SSL 
ssl.truststore.location=/var/private/ssl/client.truststore.jks 
ssl.truststore.password=masquerade

## Using SSL Configuration for Command line Clients

kafka-console-producer.sh --broker-list localhost:9093 --topic testSSL \
 --producer.config client-ssl.properties
kafka-console-consumer.sh --bootstrap-server localhost:9093 --topic testSSL \
 --consumer.config client-ssl.properties
 
## Broker SASL JAAS File

KafkaServer {
    com.sun.security.auth.module.Krb5LoginModule required
    useKeyTab=true
    storeKey=true
    keyTab=”/var/kafka_server0.keytab”
    principal=”kafka/kafka0.kia.manning.com@EXAMPLE.COM”;
};

// Zookeeper client authentication - Kafka being the client
Client {
    com.sun.security.auth.module.Krb5LoginModule required
    useKeyTab=true
    storeKey=true
    keyTab=”/var/kafka_server0.keytab”
    principal=”kafka/kafka0.kia.manning.com@EXAMPLE.COM”;
};


## Java parameter for Kafka server startup to point to the above file content

 -Djava.security.auth.login.config=/var/broker0_jaas.conf
 -Djava.security.krb5.conf=/var/krb.conf
 -Dsun.security.krb5.debug=true
 
 
## Broker SASL properties changes

listeners=PLAINTEXT://localhost:9092,SSL://localhost:9093,SASL_SSL://localhost:9094 
sasl.kerberos.service.name=kafka

## Client SASL JAAS File

    KafkaClient {
    com.sun.security.auth.module.Krb5LoginModule required
    useKeyTab=true
    storeKey=true
    keyTab="/car/kafkaclient.keytab"
    principal="kafkaclient@EXAMPLE.COM";
    };
    
## Client SASL JAAS File

security.protocol=SASL_SSL
sasl.kerberos.service.name=kafka
ssl.truststore.location=/var/kafka.client.truststore.jks
ssl.truststore.password=masquerade
ssl.key.password=masquerade

## ACL Authorizer and Super Users

authorizer.class.name=kafka.security.auth.SimpleAclAuthorizer 
super.users=User:Franz

## Kafka ACLs To Read and Write to a Topic

bin/kafka-acls --authorizer-properties \
  zookeeper.connect=localhost:2181 --add \
 --allow-principal User:Franz --allow-principal User:Hemingway \ 
 --operation Read --operation Write --topic clueful_secrets
 
## ACL ZooKeeper

zookeeper.set.acl=true

## ZooKeeper properties updates for Kerberos

authProvider.1=org.apache.zookeeper.server.auth.SASLAuthenticationProvider
requireClientAuthScheme=sasl
jaasLoginRenew=3600000

## ZooKeeper JAAS File Example

Server {
com.sun.security.auth.module.Krb5LoginModule required
useKeyTab=true
keyTab=”/var/kafka.keytab” 
storeKey=true
useTicketCache=false
principal=”kafka/kia.manning.com”; 
};

## Java parameter for ZooKeeper server startup

-Djava.security.auth.login.config=/var/zk_jaas.conf
  
## Creating a Network Bandwidth Quota for Client Clueful

kafka-configs.sh  --zookeeper localhost:2181 --alter \
--add-config 'producer_byte_rate=1048576,consumer_byte_rate=10485760' \
--entity-type clients --entity-name clueful

## Listing and Deleting a Quota for Client Clueful

kafka-configs.sh  --zookeeper localhost:2181 \
---describe \ 
--entity-type clients --entity-name clueful

kafka-configs.sh  --zookeeper localhost:2181 --alter \
--delete-config 'producer_byte_rate,consumer_byte_rate' \ 
--entity-type clients --entity-name client1

## Creating a Network Bandwidth Quota for Client Clueful

kafka-configs.sh  --zookeeper localhost:2181 --alter \
--add-config 'request_percentage=100' \
--entity-type clients --entity-name clueful 