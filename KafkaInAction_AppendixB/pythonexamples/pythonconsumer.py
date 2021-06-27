from confluent_kafka import Consumer, KafkaError #(1)

consumer = Consumer({
    'bootstrap.servers': 'localhost:9092', #(2)
    'group.id': 'testgroup',
    'auto.offset.reset': 'earliest'
})

consumer.subscribe(['python-topic']) #(3)

try:
    while True:
        message = consumer.poll(1.0) #(4)

        if message is None:
            continue
        if message.error():
            print('Error on read: %s' % message.error())
            continue
        else:
            print('Message: %s at offset: %d\n'  %
                (message.value().decode('utf-8'), message.offset()))

except KeyboardInterrupt:
    print('Shutting down consumer\n')
finally:
    consumer.close() #(5)

