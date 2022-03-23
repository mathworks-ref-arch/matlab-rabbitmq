# MATLAB and MATLAB Production Server Interface *for RabbitMQ*
MATLAB&reg; and MATLAB Production Server&trade; Interface *for RabbitMQ*
provides an interface between MATLAB as well as MATLAB Production Server and
RabbitMQ. RabbitMQ implements the Advanced Message Queuing Protocol (AMQP),
standardizes messaging using Producers, Broker and Consumers as well as
increases loose coupling and scalability. 

## Requirements

### MathWorks Products
* MATLAB Release R2017b or later
* MATLAB Production Server Release R2017b or later

### 3rd Party Products
* RabbitMQ Server 3.7.15 or above. RabbitMQ Installation Guide –
  https://www.rabbitmq.com/install-windows-manual.html
* Requires Erlang installation (version 20 or later) 

To build a required JAR file:

* Maven
* JDK/JRE 8

## Package features
This package offers two main features:

* A `MessageBroker` which subscribes to messages on RabbitMQ which it then turns
  into MATLAB Production Server function calls (with the message as input). Or
  in other words, it acts as a broker between RabbitMQ and MATLAB Production
  Server.

* A MATLAB Interface which allows subscribing to messages on- and publishing
  messages to- RabbitMQ in MATLAB. This can be used in combination with the
  previous feature to have the MATLAB code deployed to MATLAB Production Server
  respond back to RabbitMQ (e.g. to indicate the work as completed, possibly
  even publish some result) but can also be used entirely independently of
  MATLAB Production Server.

Both features require a shared Java package which first needs to be built, see
the [Getting Started](#getting-started) section below. After having completed
these steps refer to
[Documentation/MessageBroker.md](Documentation/MessageBroker.md) and
[Documentation/MATLABInterface.md](Documentation/MATLABInterface.md) to learn
more about the two separate features.

## Getting Started

Please refer to the instructions below to get started. The documents provide
detailed instructions on setting up and using the interface. The easiest way to
fetch this repository and all required dependencies is to clone the top-level
repository using:

```bash
git clone https://github.com/mathworks-ref-arch/matlab-rabbitmq.git
```

### Build RabbitMQ MATLAB Java Client Package

The RabbitMQ MATLAB Java Client Package can be build using:

```
cd Software/Java/RabbitMQClient 
mvn clean package -DskipTests
```

This will build the package skipping Java Unit Tests (`-DskipTests`). To
learn more on how to run included Java and MATLAB Unit Tests see 
[Documentation/Testing.md](Documentation/Testing.md).


## Supported Products:
1. [MATLAB&reg;](https://www.mathworks.com/products/matlab.html) (R2017b or
   later)
2. [MATLAB Compiler™](https://www.mathworks.com/products/compiler.html) and
   [MATLAB Compiler
   SDK™](https://www.mathworks.com/products/matlab-compiler-sdk.html) (R2017b or
   later)
3. [MATLAB Production
   Server™](https://www.mathworks.com/products/matlab-production-server.html)
   (R2017b or later)
4. [MATLAB Parallel Server™](https://www.mathworks.com/products/distriben.html)
   (R2017b or later)

## License, API and Features
The license for the MATLAB Interface for RabbitMQClient is available in the
[LICENSE.md](LICENSE.md) file in this GitHub repository. This package uses
certain third-party content which is licensed under separate license agreements.
See the [pom.xml](Software/Java/pom.xml) file for third-party software
downloaded at build time.

## Enhancement Request
Provide suggestions for additional features or capabilities using the following
link:   
https://www.mathworks.com/products/reference-architectures/request-new-reference-architectures.html

## Support
Email: `mwlab@mathworks.com`    

[//]: #  (Copyright 2019-2022 The MathWorks, Inc.)

