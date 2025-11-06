# Testing MATLAB and MATLAB Production Server Interface *for RabbitMQ*

The MATLAB and MATLAB Production Server Interface *for RabbitMQ* package
includes unit tests for both main features (`MessageBroker` and the MATLAB
Interface). For both main features, non-SSL secured connections as well as
SSL secured connections are tested. This requires a RabbitMQ Server instance
configured with the correct certificates.

The `Test` directory contains:

* A script `generateCertificates.sh` which can be used to generate the correct set of certificates and private keys. 

* `docker-compose.yml` as well as `20-ssl.conf` which allow running a correctly configured RabbitMQ server with SSL support in a Docker container using Docker Compose.


## Running Dockerized RabbitMQ Server for tests

From a (bash) shell:

1.  cd into the `Test` directory:

    ```console
    $ cd Test
    ```

2.  Generate the set of certificates and private keys:

    ```console
    $ ./generateCertificates.sh
    ```

3.  Start the Docker container using Docker Compose:

    ```console
    $ docker compose up -d
    ```

4.  After testing has completed use:

    ```console
    $ docker compose down
    ```

    To stop the container again.

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

Start MATLAB from the `Software/MATLAB` directory, then:

```matlabsession
>> buildtool test
```

[//]: #  (Copyright 2022-2025 The MathWorks, Inc.)