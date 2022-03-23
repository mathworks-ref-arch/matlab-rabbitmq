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

#### `rabbitmq.ConnectorProperties`
`rabbitmq.ConnectorProperties` takes various Name-Value pairs as inputs. All
options are optional, if not specified their default value is used.

|Property Name      | Default Value | Description                           |
|-------------------|---------------|---------------------------------------|
|**'host'**         | 'localhost'   | Hostname or IP of the RabbitMQ Server |
|**'port'**         | 5672          | Port the RabbitMQ Server runs on      |
|**'virtualhost'**  | '/'           | RabbitMQ Virtual Host                 |
|**'exchange'**     | 'amq.topic'   | Exchange to work with on RabbitMQ     |
|**'queuename'**    | 'RabbitMQ'    | Name of the Queue on RabbitMQ Server  |
|**'username'**     | 'guest'       | RabbitMQ username                     |
|**'password'**     | 'guest'       | RabbitMQ password                     |
|**'routingkey'**   | 'test-topic'  | Routing key to subscribe to or poll on - only used for Consumer, when working with Producer the routing key is specified on a per message basis when publishing a message |

For example:

```matlab
% Create a configuration with mostly default options but custom host and
% routingkey settings
configuration = rabbitmq.ConnectorProperties('host','myhost',...
    'routingkey','my-key');```
```

#### Configuration Files

The configuration file follows the same RabbitMQ related options as the
configuration file for the MATLAB Production Server interface:

```yaml
# Messaging connection and routing properties
messageQueue:
    queueName: RabbitMQ       # Name of the Queue on RabbitMQ Server
    host: localhost           # Hostname or IP of the RabbitMQ Server
    port: 5672                # Port the RabbitMQ Server runs on
    virtualhost: /            # RabbitMQ Virtual Host
    credentials: 
      username: guest         # RabbitMQ username
      password: guest         # RabbitMQ password
    exchange: amq.topic       # Exchange to work with on RabbitMQ
    routingkey: test-topic    # Routing key to subscribe to or poll on,
                              # only relevant/required/used when working with 
                              # Consumer, when working with Producer, the 
                              # routingkey is specified on a per message basis
                              # when publishing the message
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

Then to send a message use `sendMessage` with a routingkey and the actual
message as input:

```matlab
producer.sendMessage('my-routing-key','Hello World');
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

[//]: #  (Copyright 2022 The MathWorks, Inc.)