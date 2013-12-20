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