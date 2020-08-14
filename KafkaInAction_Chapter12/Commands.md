# Commands used in Chapter 12

## Create an interactive session with ksqlDB

docker exec -it ksqldb-cli ksql http://ksqldb-server:8088

## Create an interactive session with ksqlDB

CREATE STREAM hunterLocations (treasureId VARCHAR, latitude DOUBLE, longitude DOUBLE)
  WITH (kafka_topic='loc', key='treasureId', value_format='json', partitions=1);
  
## Create an interactive session with ksqlDB

SELECT * FROM hunterLocations
  WHERE GEO_DISTANCE(latitude, longitude, 43.6693, 92.9747, 'mi') <= 1 EMIT CHANGES;
  
## New stream events on locations

INSERT INTO hunterLocations (treasureId, latitude, longitude) VALUES ('team1', 53.6693, 93.9747);
INSERT INTO hunterLocations (treasureId, latitude, longitude) VALUES ('team2', 43.6694, 92.9847);

## Start ksqlDB in headless mode

./bin/ksql-server-start ./etc/ksql/ksql-server.properties \
--queries-file /ksql.sql
