# MATLAB Production Server Interface *for RabbitMQ*

MATLAB® interface for the RabbitMQ MATLAB Production Server Client is to support message broker that implements Advanced Message Queuing Protocol (AMQP), standardizes messaging using Producers, Broker and Consumers as well as increases loose coupling and scalability. RabbitMQ client can be deployed in distributed and federated configurations to meet high-scale, high-availability requirements. This interface allows customers to integrate MATLAB Production Server with the RabbitMQ messaging service as an asynchronous or synchronous messaging mechanism that enables reliable communication between system components and IT-MATLAB integration purposes. 

## Requirements

### MathWorks Products
* Requires MATLAB release R2017a or later.
* MATLAB Production Server™ (R2017a or later)

### 3rd Party Products
* RabbitMQ Server 3.7.15 or above. RabbitMQ Installation Guide – https://www.rabbitmq.com/install-windows-manual.html
* Requires Erlang installation (version 20 or later) 
To build a required JAR file:
* Maven
* JDK/JRE 8

## Getting Started

Please refer to the instructions below to get started. The documents provide detailed instructions on setting up and using the interface. The easiest way to fetch this repository and all required dependencies is to clone the top-level repository using:

```bash
git clone --recursive https://github.com/mathworks-ref-arch/matlab-rabbitmq.git
```

### Build RabbitMQ Java Client for MATLAB Production Server

1. The Production Server client libraries are now available for direct download from the production server product page. If you look under additional resources you should see a link for “Download the client libraries” which will take you to this page https://www.mathworks.com/products/matlab-production-server/client-libraries.html. Unzip the archive and you’ll get the client libs for all the languages. 

2. The mps_client.jar is not available on Maven Central, You will need to install it into your local repo:
```
$ mvn install:install-file -Dfile=./lib/mps_client.jar \
		-DgroupId=com.mathworks -DartifactId=mpsclient \
		-Dversion=2017a -Dpackaging=jar -DgeneratePom=true
```

3.	Build the Java package –
```
cd Software/Java/RabbitMQClient 
mvn clean package
```
4.	Verify the RabbitMQ Server Status and Web Admin Console - http://localhost:15672/#/
5.	Verify the YAML properties for message configuration and settings, such as vhost, binding, routingKey, credentials, etc.
```
# Messaging connection and routing properties
messageQueue:
  queueName: RabbitMQ
  protocol: amqp
  host: localhost
  port: 5672
  virtualhost: /
  credentials: 
    username: guest
    password: guest
  exchange: amq.topic
  routingkey: test-topic
  consumer:
    polltimeoutms: 240000
    qos: atleastonce
```
6.	Start RabbitMQ Producer and send the test message to RabbitMQ. 
In a terminal, start the Producer (aka, the MessageSender) by running the script -MessageSenderStartup.bat (On Linux, use a colon instead of a semicolon to separate items in the classpath) or using mvn for starting sender and receiver as below.
```
mvn exec:java -Dexec.mainClass=com.mathworks.messaging.MessageSender -Dexec.args="src/main/resources/mps.yaml"
```
7.	Verify the message successfully published to designated queue in RabbitMQ admin console
8.	Start the RabbitMQ Consumer (aka the MessageReceiver) by running the script MessageReceiverStartup.bat. It will receive the message from the Message Queue then forward to MATLAB Production Server to invoke the designated MATLAB function(). The consumer will keep running, waiting for messages (Use Ctrl-C to stop it).
9.	 Verify the MATLAB Production Server successfully receive the message and MATLAB function is invoked successfully
Use the `/Software/MATLAB/MPSreceive.m` function to accept the incoming RabbitMQ message and check the status on the Production SDK Compiler Test Client interface through URL: http://localhost:9910/demo

#### Note
Please see the RabbitMQ Authentication, Authorization, Access Control @ https://www.rabbitmq.com/access-control.html on how to config the credentials for remote access, TLS support, authentication, etc.


### Class Usage for MessageQueueHandler

1. Load message queue related properties from yaml file

```java
headerproperties = mapper.readValue(fileName, ConnectorProperties.class);
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
7. Consume message from queue - Start the MessageReceiver to receive messages through MessageQueueHandler.

```java
mqHandler = new MessageQueueHandler();
mqHandler.setupConnectionFactoryFromConfig(new File(configFile));
mqHandler.setupMessageChannel();
mqHandler.setupMPS;
mqHandler.receiveMessage();
```
			
	
## Supported Products:
1. [MATLAB](https://www.mathworks.com/products/matlab.html) (R2017a or later)
2. [MATLAB Compiler™](https://www.mathworks.com/products/compiler.html) and [MATLAB Compiler SDK™](https://www.mathworks.com/products/matlab-compiler-sdk.html) (R2017a or later)
3. [MATLAB Production Server™](https://www.mathworks.com/products/matlab-production-server.html) (R2017a or later)
4. [MATLAB Parallel Server™](https://www.mathworks.com/products/distriben.html) (R2017a or later)

## License, API and Features
The license for the MATLAB Interface for RabbitMQClient is available in the [LICENSE.md](LICENSE.md) file in this GitHub repository. This package uses certain third-party content which is licensed under separate license agreements. See the [pom.xml](Software/Java/pom.xml) file for third-party software downloaded at build time.

## Enhancement Request
Provide suggestions for additional features or capabilities using the following link:   
https://www.mathworks.com/products/reference-architectures/request-new-reference-architectures.html

## Support
Email: `mwlab@mathworks.com`    

[//]: #  (Copyright 2019 The MathWorks, Inc.)

