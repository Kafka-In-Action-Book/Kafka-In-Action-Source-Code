from confluent_kafka import Producer  #(1)

producer = Producer({'bootstrap.servers': 'localhost:9092'}) #(2)

def result(err, message): #(3)
    if err:
        print('%% Producer failure: %s\n' % err)
    else:
        print('Producer info: topic=%s, partition=[%d], offset=%d\n' %
        <linearrow /> (message.topic(), message.partition(), message.offset()))

messages = ["hello python", "hello again"] #(4)

for msg in messages:
    producer.poll(0)
    producer.produce("python-topic", value=msg.encode('utf-8'), callback=result) #(5)

producer.flush() #(6)
