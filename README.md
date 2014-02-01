flying-fortress C108
====================

Transactions with Kafka Message Publishing:
------------------------------------------

Flying Fortress C108 was the designation of the United States B-17 Flying Fortress
heavy bombers which were converted to transport aircraft during World War II.

http://en.wikipedia.org/wiki/Boeing_C-108_Flying_Fortress

Problem Statement:
------------------
Kafka does not support transactions with regard to message publishing.
Every message publication operation is atomic i.e it either succeeds or fails.
but in a scenario where one has to publish a group of  messages with a condition that
either ALL get published or none of them get published, this kind of atomicity is not
currently available with the kafka client library.

The Flying Fortress C-108 , transports multiple messages in one go to simulate transaction
capability for message publishing.

How does it Work?
-----------------
When a transaction is opened in a thread, a in-memory buffer is created.
messages which need to be published as part of a transaction are added to the buffer.
on commit, all the added messages are clubbed together into a SINGLE message and a publisher
resource emits out this clubbed message. This operation is atomic.
on rollback, the buffer is cleared out.

Notes:
Since, an internal buffer is used, this is intended for use when messages in txn have a small payload.

Ready to Fly the Fortress? give it a spin
----------------------------------------
configuration = new TransactionConfiguration();
configuration.setKafkaProducerConfig(new KafkaProducerConfig());
configuration.setKafkaConsumerConfig(new KafkaConsumerConfig());
TransactionManager.init(configuration);

MessageTransaction.start();
MessagePublisher.addMessage(new Message(payload), destination_1);
MessagePublisher.addMessage(new Message(payload), destination_2);
MessageTransaction.commit();

//now create a MessageSubscriber to the interested destination
//and register yourself as a callback.
//Refer: to the test class 'KafkaTransactionTest'.

Status:
-------
first version ready.

MainClasses:
-------
TransactionConfiguration
TransactionManager
MessageSubscriber
MessagePublisher

todo:
-----
automate version change for pom.xml
stress testing.