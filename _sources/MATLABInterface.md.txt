# RabbitMQ MATLAB Interface

## Installation

### Building the Java package
Refer to the [Getting Started Section in
README.md](../README.md#build-rabbitmq-matlab-java-client-package) for
instructions on building the required JAR-file.

### Configuring MATLABPATH and Java class path `startup.m`
Before using the package in MATLAB, the Java client library needs to be loaded
on the MATLAB Java class path and the `Software/MATLAB/app` directory needs to
be added to the MATLABPATH. This can be accomplished by running the `startup`
function from the `Software/MATLAB` directory.

Note that this will load the JAR-file on the *dynamic* class path. Not all
functionality is supported when the JAR-file is loaded on the dynamic class
path, see below. See the [MATLAB
documentation](https://www.mathworks.com/help/matlab/matlab_external/java-class-path.html)
to learn more about the Java class path in general and on how to add the
JAR-file to the static class path if desired/needed.

## Usage

### Connection Properties
When working with `rabbitmq.Producer` and `rabbitmq.Consumer` in MATLAB, various
connection properties need to be specified. These can be specified with the help
of the `rabbitmq.ConnectorProperties` class or using YAML configuration files.

#### rabbitmq.ConnectorProperties class

**rabbitmq.ConnectorProperties** has various properties:

:host: Hostname or IP of the RabbitMQ Server.\
  *Default:* `"localhost"`
:port: Port the RabbitMQ Server runs on.\
  *Default:* `5672`
:virtualhost: RabbitMQ Virtual Host.\
  *Default:* `"/"`
:exchange: Exchange to work with on RabbitMQ.\
  *Default:* Default `rabbitmq.ExchangeProperties`
:queue: Queue to work with on RabbitMQ Server.\
  *Default:* Default `rabbitmq.QueueProperties`
:credentials: RabbitMQ server credentials.\
  *Default:* Default `rabbitmq.Credentials`
:routingkey: Routing key to subscribe to or poll on - only used for Consumer, when working with Producer the routing key is specified on a per message basis when publishing a message.\
  *Default:* ``"test-topic"``

Where, as can be seen, properties `exchange`, `queue` and `credentials` are 
in turn other MATLAB classes:

**rabbitmq.ExchangeProperties**

:name: Name of the exchange.\
  *Default:* `"amq.topic"` 
:type: Type of exchange.\
  *Default:* `"topic"`
:create: See [the note on create in MessageBroker](noteoncreate).\
  *Default:* `true`
:durable: Only relevant if ``create`` = ``true``, create a durable exchange or not\
  *Default:* `true`
:autoDelete: Only relevant if ``create`` = ``true``, create an autoDelete exchange or not\
  *Default:* `false`
:internal: Only relevant if ``create`` = ``true``, create an internal exchange or not\
  *Default:* `false`
:arguments: Only relevant if ``create`` = ``true``, allows setting additional arguments. Use the `put` method to add additional arguments. \
  *Default:* Empty `HashMap`

**rabbitmq.QueueProperties**

:name: Name of the queue.\
  *Default:* `"RabbitMQ"`
:create: See See [the note on create in MessageBroker](noteoncreate).\
  *Default:* `true`
:durable: Only relevant if ``create`` = ``true``, create a durable queue or not.\
  *Default:* `false`
:exclusive: Only relevant if ``create`` = ``true``, create an exclusive queue or not.\
  *Default:* `false`
:autoDelete: Only relevant if ``create`` = ``true``, create an autoDelete queue or not.\
  *Default:* `false`
:arguments: Only relevant if ``create`` = ``true``, allows setting additional arguments. Use the `put` method to add additional arguments. \
  *Default:* Empty `HashMap`

**rabbitmq.Credentials**

:username: Username\
  *Default:* `"guest"`
:password: Password\
  *Default:* `"guest"`

Setting properties values can be done through traditional MATLAB class syntax:

```matlab
% Create an instance
c  = rabbitmq.ConnectorProperties;
% Set a property value
c.host = "myHost";
% Set a property of one of the "nested" settings
c.queue.name = "myQueue";
```

But (with the exception of the `arguments` property) they can also be set by providing Name-Value pairs corresponding to property
name and the value it is to be set to as inputs to the constructor.

```matlab
% Create instance and immediately set host to myHost and port to 1234
c  = rabbitmq.ConnectorProperties("host","myHost","port",1234);
```

To immediately set "nested" settings, use the same trick on one of the nested 
classes:

```matlab
% Create the QueueProperties with name and durable set
qp = rabbitmq.QueueProperties("name","myQueue","durable",true);
% Create ConnectorProperties with these QueueProperties set
c  = rabbitmq.ConnectorProperties("queue",qp);

% Or this could even be done one big single call then
c = rabbitmq.ConnectorProperties("queue", ...
    rabbitmq.QueueProperties("name","myQueue","durable",true));
```

To add additional arguments through the `arguments` property, use the put method:

```matlab
% Create QueueProperties with some properties already set
c = rabbitmq.ConnectorProperties("queue", ...
    rabbitmq.QueueProperties("name","myQueue","durable",true));
% Then in a separate step add additional arguments, for example
c.queue.arguments.put('x-max-length',42);
% Similarly for exchange
c.exchange.arguments.put('alternate-exchange', 'my-ea');
```

#### Configuration Files

The configuration file follows the same RabbitMQ related options as the
configuration file for the MATLAB Production Server interface:

```yaml
# Messaging connection and routing properties
messageQueue:
  queue:
    name: RabbitMQ          # Name of the Queue on RabbitMQ Server
    create: true            # Creates/verifies whether queue exists
    durable: false          # Work with a durable queue or not
    exclusive: false        # Work with an exclusive queue or not
    autoDelete: false       # Work with an auto delete queue or not
    arguments:              # Set additional arguments, can be omitted entirely
      x-max-length: 42      # For example, set the maximum queue length
  host: localhost           # Hostname or IP of the RabbitMQ Server
  port: 5672                # Port the RabbitMQ Server runs on
  virtualhost: /            # RabbitMQ Virtual Host
  credentials: 
    username: guest         # RabbitMQ username
    password: guest         # RabbitMQ password
  exchange:
    name: amq.topic         # Exchange to work with on RabbitMQ
    create: true            # Creates/verifies whether exchange exists
    durable: true           # Work with a durable exchange or not
    autoDelete: false       # Work with an auto delete exchange or not
    internal: false         # Work with an internal exchange or not
    arguments:              # Set additional arguments, can be omitted entirely
      alternate-exchange: my-ea # For example alternate-exchange
  routingkey: test-topic    # Routing key to subscribe to
```

```{note}
The `arguments` option can be omitted entirely for both `queue` and `exchange` if it is not necessary to set additional arguments. There is no fixed set of arguments which can be added and the entered argument names are not checked by the interface; they are passed on the server as-is. Check the RabbitMQ documentation to learn more about which exact arguments can be configured.
```

### RabbitMQ Producer for publishing messages `rabbitmq.Producer`
To work with `rabbitmq.Producer` in MATLAB, first create an instance with
`rabbitmq.ConnectorProperties` or configuration YAML-file as input:

```matlab
% Create a Producer based on a configuration file
producer = rabbitmq.Producer(fullfile(rabbitMQProjectRoot,'config','example.yaml'));
% Or alternatively using ConnectorProperties
configuration = rabbitmq.ConnectorProperties();
producer = rabbitmq.Producer(configuration);
```

Then to send a message use `publish` with a routingkey and the actual
message as input:

```matlab
producer.publish('my-routing-key','Hello World');
```

### RabbitMQ Consumer for receiving messages `rabbitmq.Consumer`
`rabbitmq.Consumer` offers two approaches for receiving messages from RabbitMQ
in MATLAB. An event-based approach and a polling approach. These two different
approaches correspond to the push- and pull approaches respectively, as
[described in the RabbitMQ Java
API](https://www.rabbitmq.com/api-guide.html#consuming). As noted in this
RabbitMQ Java API documentation the push (i.e. in MATLAB event based approach)
is recommended. However, in MATLAB this will require the JAR-file to be loaded
on the *static* Java class path rather than on the *dynamic* Java class path.

#### Event Based Approach

```matlab
function consumer = EventBasedConsumerExample
    % Create the Consumer  with configuration file as input. Working with
    % rabbitmq.ConnectorProperties is supported here as well. Further set
    % eventBased = true to indicate to work with the event based approach 
    consumer = rabbitmq.Consumer( ...
        fullfile(rabbitMQProjectRoot,'config','example.yaml'), ...
        true);

    % Add a listener to the MessageReceived event
    addlistener(consumer,'MessageReceived',@onMessageReceived)


function onMessageReceived(~,message)
    % In the listener, as an example simply print the message
    fprintf('Message received: %s\n',message.message);
```

#### Polling Approach

```matlab
function PollBasedConsumerExample
    % Create the Consumer  with configuration file as input working with
    % rabbitmq.ConnectorProperties is supported here as well. Further set
    % eventBased = false to indicate to work with the polling approach
    consumer = rabbitmq.Consumer( ...
        fullfile(rabbitMQProjectRoot,'config','example.yaml'), ...
        false);
    % Now actually start polling
    
    % In this example receive up to 3 messages then stop
    numReceived = 0;
    fprintf('Polling for messages...');
    % As long as we have not received three messages yet
    while numReceived < 3
        % Poll for a message
        message = consumer.pollMessage();
        if ~isempty(message) % If a message was received 
            % Display it
            fprintf('\nMessage Received: %s\n',message);
            % Increase the counter
            numReceived = numReceived + 1;
            fprintf('Polling for messages...');
        end
        fprintf('.')
        % Pause for a second before polling again
        pause(1);
    end
    % After 3 messages have been received
    fprintf('stopped.\n');
```

## Deployment

When using this MATLAB Package in a MATLAB function deployed to MATLAB
Production Server in order to send back replies/results to a RabbitMQ client (or
in any other MATLAB Compiler (SDK) deployed component) it is important the
correct functionality gets compiled into the deployed component. The MATLAB
Compiler (SDK) dependency analysis should be able to automatically detect and
include the MATLAB dependencies by itself but the JAR-file must be added
manually to the component by adding it to the "Additional files required for
your component to run" in the Compiler App or by using the `-a` option when
working with `mcc` on the command line. 

Any JAR-file compiled into a deployed component will be automatically added to
the Java class path at runtime and any functions compiled into the component
will be automatically on the MATLABPATH at runtime; so there is no need then to
explicitly call `startup` in the deployed component. If possible the JAR-file is
added to the *static* class path but there may be circumstances in which this is
not possible and then it is loaded on the *dynamic* class path; this may make
the event based consumer approach unavailable.

[//]: #  (Copyright 2022-2023 The MathWorks, Inc.)