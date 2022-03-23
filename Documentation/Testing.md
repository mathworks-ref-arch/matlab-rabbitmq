# Testing MATLAB and MATLAB Production Server Interface *for RabbitMQ*

The MATLAB and MATLAB Production Server Interface *for RabbitMQ* package
includes unit tests for both main features (`MessageBroker` and the MATLAB
Interface). Both sets of tests require an actual RabbitMQ Server, setup in such
a way that it is compatible with the configuration in
[`mps.yaml`](Software/Java/RabbitMQClient/src/main/resources/mps.yaml) and the
defaults as used by `rabbitmq.ConnectorProperties`. Luckily it is easy to run
such a server. A locally run `rabbitmq:3-management` Docker image, matches this
configuration.

## Running Dockerized RabbitMQ Server for tests
To start the server use:

```
docker run -d --rm --name rabbitmq-for-matlab -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

Where `-p 5672:5672` exposes the main RabbitMQ port as used for actual messaging
and `-p 15672:15672` exposes the Management Console, making it available on
[http://localhost:15672/](http://localhost:15672/). `-d` runs the container as a
daemon, `--rm` disposes the container after it has been stopped and `--name
rabbitmq-for-matlab` is just to name the container to make it easier to stop.

To stop it use:

```
docker stop rabbitmq-for-matlab
```

## Running Java Unit Tests
To run the Java Unit Tests:
```
cd Software/Java/RabbitMQClient
mvn test
```

## Running MATLAB Unit Tests
The MATLAB Unit Tests require the JAR file to have been loaded on the *static*
Java class path. Both the polling- as well as the event based subscription
approach are tested.

Start MATLAB, then:

```matlab
cd Software/MATLAB/test
runtests
```
[//]: #  (Copyright 2022 The MathWorks, Inc.)