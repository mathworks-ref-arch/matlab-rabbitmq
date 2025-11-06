# RabbitMQ `MessageBroker` for MATLAB Production Server

## Architecture
`MessageBroker`, is essentially a RabbitMQ (AMQP) Consumer which can receive
messages from a RabbitMQ Server which it then passes along as input to a
function deployed ot MATLAB Production Server. The MATLAB function is assumed to
have exactly one input which is the message as character array.

```{mermaid}
flowchart
  client(Message Producer) -- publish message --> server[(RabbitMQ Server)]-- deliver message --> MessageBroker[MessageBroker] --"call with message as input"--> mps(MATLAB Production Server)
  MessageBroker -. subscribe .-> server
```

As RabbitMQ supports various protocols (AMQP as native protocol but also MQTT
and STOMP as plugins), the Message Producer can be any client supporting any of
those protocols.

## Message Datatype
RabbitMQ messages _in general_ are simply raw bytes, this package implicitly
assumes however that all messages are UTF-8 encoded strings. Consider encoding
your messages in JSON format if "more complex" data structures need to be
represented in the messages. On the MATLAB end the `jsondecode` function can be
used to parse the JSON string into a MATLAB structure.

## Dataflow
A full workflow with this package could look like the following:

```{mermaid}
sequenceDiagram
    autonumber
    participant MessageSender
    participant RabbitMQ Server
    participant MessageBroker
    participant MATLAB Production Server
    note over MessageSender,MATLAB Production Server: MessageBroker Startup
    MessageBroker-->>RabbitMQ Server: subscribes
    note over MessageSender,MATLAB Production Server: For each message/request sent by MessageSender
    opt
        MessageSender-->>RabbitMQ Server: binds
    end
    MessageSender->>RabbitMQ Server: message
    RabbitMQ Server->>+MessageBroker: message
    MessageBroker->>+MATLAB Production Server: message
    opt
        MATLAB Production Server->>-RabbitMQ Server: reply
        deactivate MessageBroker
        RabbitMQ Server->>MessageSender: reply
    end
```

1. Upon startup `MessageBroker` connects to the configured RabbitMQ Server and
   subscribes to the specified `queue` on the specified `exchange` with
   specified `routingkey`.

2. If a MessageSender expects a reply of the function deployed to MATLAB
   Production Server through RabbitMQ, it first subscribes to the RabbitMQ
   server as well, if using the same `queue` on the same `exchange` make sure to
   use a different `routingkey` (to avoid infinite loops where the response
   would trigger another MATLAB Production Server call).

3. The MessageSender (this can literally be the `MessageSender` example client
   included in this package or any other RabbitMQ Client) sends a message with a
   `routingkey` to a `queue` on the RabbitMQ Server.

4. If the `queue` and `routingkey` match the ones `MessageBroker` has subscribed
   to, the RabbitMQ Server delivers to message to `MessageBroker`.

5. `MessageBroker` uses MATLAB Production Server Java client to call the
   specified `function` in the specified `archive` with the message as input.
   MATLAB Production Server will then process this request. If the package is
   used to simply trigger a function call without outputs the flow ends here.

6. If the MATLAB code needs to return an output to the client, the MATLAB code
   can use `rabbitmq.Producer` to send a message with the `routingkey` the
   client is bound to, to the RabbitMQ Server.

    ```{note}
    Note: this bypasses `MessageBroker`. `MessageBroker` does not do anything
    with the "MATLAB style output" of the function deployed to MATLAB
    Production Server. The MATLAB code deployed to MATLAB Production Server
    has to explicitly use `rabbitmq.Producer` in the MATLAB code if a reply is
    to be send over RabbitMQ.
    ```

7. If the `routingkey`, `queue` and `exchange` match the one the client has
   subscribed to, the RabbitMQ server will deliver the reply to the client.

## Installation
### Building the Java package
Refer to the [Getting Started Section in
README.md](../README.md#build-rabbitmq-matlab-java-client-package) for
instructions on building the required JAR-file.

### Further configuration and testing the setup

1.	Before continuing it is good to verify that the RabbitMQ Server is up and
running correctly and can be accessed, for example by accessing the Web Admin
console which typically runs on port 15672, so for a local server check
http://localhost:15672/.

    ```{hint}
    See the [RabbitMQ Authentication, Authorization, Access Control 
    documentation](https://www.rabbitmq.com/access-control.html) on how to 
    configure credentials for remote access, TLS support, authentication, etc.
    ```

2.  For an initial test, MATLAB Compiler SDK's MATLAB Production Server testing
    interface can be used rather than an actual MATLAB Production Server
    instance running compiled CTF archives:

    1. In MATLAB start the `Production Server Compiler` App. 
    2. Enter `demo` as archive name.
    3. Add `Software/MATLAB/examples/MPSreceive.m` as exported function.
    4. Click `Test Client`.
    5. Click `Start` to start the test server.

3.	Open `Software/Java/RabbitMQClient/src/main/resources/mps.yaml` and update
the options to match your configuration. 

    ```{note}
    The `arguments` for `queue` and `exchange` will likely have to be removed, they are not often used and shown here mainly for illustrative purposes to show *where* these can be added *if* they are needed. There is no fixed set of arguments which can be added and argument names are not verified by `MessageBroker`; they are send to the server as-is. Check the RabbitMQ documentation to learn more about the argument its supports.
    ```

    ```{note}
    The `sslcontext` section should be omitted entirely if the amqp channel is not SSL/TLS secured at all. If the channel is SSL/TLS secured, the section must be present and the `server` section must be configured. The `client` section is optional; it must be configured if the server requires client certificates but is omitted if it does not. Also see [](./SSLTLS.md) to learn more about the SSL/TLS configuration.
    ```

    ```yaml
    # MATLAB Production Server connection properties
    mps:
      protocol: http                 # Protocol used by the MPS Instance
      host: localhost                # Hostname or IP of the MPS Instance
      port: 9910                     # Port the MPS Instance runs on
      archive: demo                  # Name of the CTF containing the function which
                                     # is to be called on MPS when a message received
      function: MPSreceive           # Function inside the archive which is to be called
      timeoutms: 120000              # Timeout on the request to MATLAB Production Server
                                     # MessageBroker will log an error if the request
                                     # to MATLAB Production Server did not complete within
                                     # this time

    # Messaging connection and routing properties
    messageQueue:
      queue:
        name: RabbitMQ               # Name of the Queue on RabbitMQ Server
        create: true                 # Creates/verifies whether queue exists
        durable: false               # Work with a durable queue or not
        exclusive: false             # Work with an exclusive queue or not
        autoDelete: false            # Work with an auto delete queue or not
        arguments:                   # Set additional arguments, can be omitted entirely
          x-max-length: 42           # For example, set the maximum queue length
      host: localhost                # Hostname or IP of the RabbitMQ Server
      port: 5672                     # Port the RabbitMQ Server runs on
      virtualhost: /                 # RabbitMQ Virtual Host
      credentials: 
        username: guest              # RabbitMQ username
        password: guest              # RabbitMQ password
      exchange:
        name: amq.topic              # Exchange to work with on RabbitMQ
        create: true                 # Creates/verifies whether exchange exists
        durable: true                # Work with a durable exchange or not
        autoDelete: false            # Work with an auto delete exchange or not
        internal: false              # Work with an internal exchange or not
        arguments:                   # Set additional arguments, can be omitted entirely
          alternate-exchange: my-ea  # For example alternate-exchange
      routingkey: test-topic         # Routing key to subscribe to
      sslcontext:                    # SSL/TLS Configuration, omit this section entirely when not working with SSL
        protocol: TLSv1.2            # Exact SSL/TLS protocol (version)
        server:                      # Server trust store configuration
          truststore: /some/location # Location of the trust store containing the server certificate chain
          passphrase: rabbitstore    # Passphrase/password of the trust store
          type: JKS                  # Type of trust store
          hostnameVerification: true # Enable hostname verification
        client:                      # Client Certificate Configuration. Omit this section entirely if your server does not require client certificates
          keystore: /some/location   # Location of the keystore containing client certificate and private key
          passphrase: supersecret    # Passphrase/password of the keystore
          type: PKCS12               # Type of keystore      
    ```

    ```{note}
    ---
    name: noteoncreate
    ---
    For both `queue` and `exchange`, `name` must be set and the `create` option has the following effects:

    *   If set to `false`:
        
        *   The broker will not try to create the queue or exchange with the specified `name`. 
            They must already exist with the correct name on the server end for the broker to work 
            correctly.
        *   The other settings (`durable`, `autoDelete`, etc.) are ignored entirely and can also simply
            be omitted from the configuration file then. 
        *   This allows a certain flexibility if from the client end the exact configuration does
            not matter and is allowed to be set by the server or other clients entirely. Do **not** use this option
            if it is imperative for the client that the queue/exchange is configured with specific
            options. In that case set `create` to `true` and specify the exact settings such that the broker will 
            refuse to start if there is a configuration mismatch.

    *   If set to `true`:

        *   The broker will ensure that the specified queue or exchange with the specific settings
            exists or error out:

            *   If there is no exchange/queue with the specified name yet, it is created with the
                settings as specified in the other properties (`durable`, `autoDelete`, etc.).
            *   If there is an exchange/queue with the specified name already, and the existing instance
                was created with the same settings (`durable`, `autoDelete`, etc.), the existing
                instance is used.
            *   If there is an exchange/queue with the specified name already, but there is a mismatch
                in settings the broker will error out and refuse to start.
    ```

    If indeed working with the MATLAB Compiler SDK MATLAB Production Server testing
    interface on your local machine configured as described in step 4, the `MATLAB 
    Production Server connection properties` settings should be correct already. 

4.	To verify it is possible to successfully send a message to the RabbitMQ
Server using this configuration, the `MessageSender` application can be used. It
can be started using `MessageSenderStartup.bat` on Windows or using mvn on any
system:

    ```
    mvn exec:java -Dexec.mainClass=com.mathworks.messaging.MessageSender -Dexec.args="src/main/resources/mps.yaml"
    ```

    In the client enter a message and routingkey, then in the RabbitMQ Admin Web Console
    verify that indeed a message was received on the configured `queue`. (Ctrl+C can be
    used to stop the application).

5.	To complete the workflow, start `MessageBroker` by running
`MessageBrokerStartup.bat` on Windows or alternatively run it using mvn:

    ```
    mvn exec:java -Dexec.mainClass=com.mathworks.messaging.MessageBroker -Dexec.args="src/main/resources/mps.yaml"
    ```

    This will receive messages from the message queue and then forward these to MATLAB
    Production Server to invoke the designated MATLAB `function`. The consumer will keep
    running, waiting for messages (Use Ctrl+C to stop the application).

6.  Now when using `MessageSender` to send a message with the correct
    `routingkey` it should be received by `MessageBroker` which should then call
    the MATLAB Production Server (test) server. If indeed working with the test
    server inside MATLAB, the message should be displayed in the MATLAB Command
    Window.

[//]: #  (Copyright 2022-2023 The MathWorks, Inc.)