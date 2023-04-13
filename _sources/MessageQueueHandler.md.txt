# MessageQueueHandler
This document further explains how the `MessageQueueHandler` class could be used in other Java projects when not running `MessageBroker` as a standalone application.

## Class Usage for MessageQueueHandler

1. Load message queue related properties from yaml file

```java
properties = mapper.readValue(fileName, ConnectorProperties.class);
```

2. Set up the message queue connection factory based on the yaml properties

```java
factory.setHost((String) OverrideHandler.getOverride("MESSAGEQUEUE_HOST",properties.getMessageQueue().getHost
factory.setPort((int) OverrideHandler.getOverride("MESSAGEQUEUE_PORT",properties.getMessageQueue().getPort()));
factory.setVirtualHost((String) OverrideHandler.getOverride("MESSAGEQUEUE_VIRTUAL_HOST", properties.getMessageQueue().getVirtualhost()));
```

3. Set up the message connection channel from the connection factory

```java
connection = factory.newConnection();
QUEUE_NAME = properties.getMessageQueue().getQueueName();
channel = connection.createChannel();
// set up Exchange/Queue Binding 
channel.exchangeDeclare((String) OverrideHandler.getOverride("MESSAGEQUEUE_EXCHANGE", 
	properties.getMessageQueue().getExchange()),"topic",true);
channel.queueDeclare(QUEUE_NAME, false, false, false, null);
channel.queueBind(QUEUE_NAME, (String) OverrideHandler.getOverride("MESSAGEQUEUE_EXCHANGE", 
	properties.getMessageQueue().getExchange()),
	(String) OverrideHandler.getOverride("MESSAGEQUEUE_ROUTING_KEY",
	properties.getMessageQueue().getRoutingkey()));
```	        

4. Send message through either console or a data file

```java
channel.basicPublish(properties.getMessageQueue().getExchange(), routingKey, null, 	
	message.getBytes("UTF-8"));
//Before receiving the message, set up the connection to the MATLAB MPS servers		
// Configure the MPS connection
MPSClient client = MPSClient.getInstance(); // Fetches a singleton
client.initialize(properties);
```

5. Receive messages then forward to the MATLAB MPS.	

```java
DeliverCallback deliverCallback = (consumerTag, delivery) -> {
	 String message = new String(delivery.getBody(), "UTF-8");
	 LOG.info("message to be received:"+ message);
	  MPSClient client = MPSClient.getInstance();
	       client.sendToMPS(message); }
	       channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
```

6. Publish messages to queue - Start MessageSender to send the message through MessageQueueHandler.
```java
			
mqHandler = new MessageQueueHandler();
mqHandler.setupConnectionFactoryFromConfig(new File(configFile));
mqHandler.setupMessageChannel();
String message = System. console(). readLine("Message:");
mqHandler.sendMessage(routingKey, message);
```
7. Consume message from queue - Start the MessageBroker to receive messages through MessageQueueHandler.

```java
mqHandler = new MessageQueueHandler();
mqHandler.setupConnectionFactoryFromConfig(new File(configFile));
mqHandler.setupMessageChannel();
mqHandler.setupMPS;
mqHandler.receiveMessage();
```
			
[//]: #  (Copyright 2000-2022 The MathWorks, Inc.)