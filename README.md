# RabbitMQ-AMQP

The Advanced Message Queuing Protocol (AMQP) is an open standard application layer protocol for message-oriented middleware.

This repository contains NeoLoad Advanced Actions that allows performance testers using NeoLoad to send messages to RabbitMQ using the AMQP protocol.

| Property           | Value             |
| ----------------   | ----------------  |
| Maturity           | Experimental      |
| Support            | Not supported by Neotys      |
| Author             | Neotys Professional Services |
| License            | [BSD Simplified](https://www.neotys.com/documents/legal/bsd-neotys.txt) |
| NeoLoad            | 5.2 (Enterprise or Professional Edition w/ Integration & Advanced Usage and NeoLoad Web option required)|
| Requirements       |    |
| Bundled in NeoLoad | No |
| Download Binaries  | See the [latest release](https://github.com/Neotys-Labs/RabbitMQ-AMQP/releases/latest)

## Installation

1. Download the [latest release](https://github.com/Neotys-Labs/RabbitMQ-AMQP/releases/latest)
1. Read the NeoLoad documentation to see [How to install a custom Advanced Action](https://www.neotys.com/documents/doc/neoload/latest/en/html/#25928.htm)

## Usage

MQSend action requires:
1. A 'MQConnect' action is executed before the action to establish the connection to RabbitMQ.
2. A 'MQDisconnect' action is executed after the action to close the connection.

A good practice is to put 'MQConnect' in the 'Init' container of the Virtual User Path and the 'MQDisconnect' in the 'End' container. It ensures that the connection is estably once per Virtual User.

## Action 'MQConnect'

Connects to Rabbit MQ.

| Name                     | Description       |
| ---------------          | ----------------- |
| connectionName           | Name to give to the opened connection  
| hostname                 | Name/IP address of the RabbitMQ server 
| port                     | Port used to connect , send & receive message .Default value= 5672 |
| virtualhost              | Namespace for objects like exchanges, queues & bindings |
| username                 | Username for connecting to the queue |
| password                 | Password for connecting to the queue |

Status Codes:
* NL-MQConnect_Error: Any error while connecting to RabbitMQ. 

## Action 'MQSend'

Creates a channel and performs a basicPublish.

| Name                     | Description       |
| ---------------          | ----------------- |
| connectionName           | Name of the connection as specified in the 'MQConnect' action |
| exchangeChannelName      | Name of the exchange channel |
| queueName      	       | (optional) Name of Queue |
| routingkey               | (optional) key for routing message to queue |
| message                  | Message to send to the queue |

Status Codes:
* NL-MQSend_ErrorR:  Any error while sending message.

## Action 'MQReceive'

Receives messages from a queue.

| Name                     | Description       |
| ---------------          | ----------------- |
| connectionName           | the name of the connection as specified in the 'MQConnect' action |
| queueName                | Name of the queue |

Status Codes:
* NL-MQReceive_Error:  Any error while sending message.

## Action 'MQDisconnect'

Disconnects from a queue.

| Name                     | Description       |
| ---------------          | ----------------- |
| connectionName           | the name of the connection as specified in the 'MQConnect' action |

Status Codes:
* NL-MQDisconnect_Error:  Any error while disconnecting from the queue.

## Action 'MQMonitor'

Returns Monitoring information related to the connection:
Number of messages published, rejected, consumed and acknowledged, number of channels

| Name                     | Description       |
| ---------------          | ----------------- |
| connectionName           | the name of the connection as specified in the 'MQConnect' action |

