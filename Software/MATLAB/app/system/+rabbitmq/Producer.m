classdef Producer < rabbitmq.object
% PRODUCER Allows publishing messages to a RabbitMQ Server.
%
% Producer Methods:
%
%   Producer    - Constructor
%   publish     - Publish a message

% Copyright 2020-2022 The MathWorks, Inc.

    methods        
        function obj = Producer(config)
            % PRODUCER Creates a Producer instance. Either requires a
            % rabbitmq.ConnectorProperties configuration or YAML
            % configuration file as input.
            % 
            % The format of the YAML file is as follows:
            %
            %   messageQueue:
            %     queue:
            %       name: RabbitMQ          # Name of the Queue on RabbitMQ Server
            %       create: true            # Creates/verifies whether queue exists
            %       durable: false          # Work with a durable queue or not
            %       exclusive: false        # Work with an exclusive queue or not
            %       autoDelete: false       # Work with an auto delete queue or not
            %     host: localhost           # Hostname or IP of the RabbitMQ Server
            %     port: 5672                # Port the RabbitMQ Server runs on
            %     virtualhost: /            # RabbitMQ Virtual Host
            %     credentials: 
            %       username: guest         # RabbitMQ username
            %       password: guest         # RabbitMQ password
            %     exchange:
            %       name: amq.topic         # Exchange to work with on RabbitMQ
            %       create: true            # Creates/verifies whether exchange exists
            %       durable: true           # Work with a durable exchange or not
            %       autoDelete: false       # Work with an auto delete exchange or not
            %       internal: false         # Work with an internal exchange or not
            %     routingkey: test-topic    # Routing key to subscribe to
            %
            %   Examples:
            %
            %       % Create a configuration with default settings, except
            %       % as an example override the host setting
            %       configuration = rabbitmq.ConnectorProperties('host','myhost');
            %       % Instantiate the producer
            %       producer = rabbitmq.Producer(configuration);
            %
            %       % Create a producer based on a configuration file
            %       producer = rabbitmq.Producer('myConfiguration.yaml');
            %
            %   See Also: CONNECTORPROPERTIES
            obj.Handle = com.mathworks.messaging.MessageQueueHandler();
            if isa(config,'rabbitmq.ConnectorProperties')
                config = config.Handle;
            end
            obj.Handle.setupConnectionFactoryFromConfig(config);
            obj.Handle.setupMessageChannel();
        end
        
        
        function publish(obj, routingKey, message)
            % PUBLISH Publish a message with a given routing key.
            %
            %   Example:
            %
            %       producer.publish('my-topic','Hello World');
            obj.Handle.sendMessage(routingKey, message);
        end

        function publishWithHeaders(obj, routingKey, headers, message)
            % PUBLISHWITHHEADERS Publish a message with headers.
            %
            %   Example:
            %
            %       producer.publishWithHeaders('my-topic', ...
            %           {'HeaderName1',HeaderValue1,'HeaderName2',HeaderValue2}, ...
            %           'Hello World');
            
            % Create a HashMap with the header values
            headerMap = java.util.HashMap;
            for i=1:2:length(headers)
                headerMap.put(headers{i},headers{i+1});
            end
            % Create a properties builder
            propertiesBuilder = javaObject('com.rabbitmq.client.AMQP$BasicProperties$Builder');
            % Set the header and build the properties
            props = propertiesBuilder.headers(headerMap).build;
            % Call send message with the properties
            obj.Handle.sendMessage(routingKey, props, message);
        end

    end
    
end