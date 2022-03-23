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
            %     messageQueue:
            %         queueName: RabbitMQ       # Name of the Queue on RabbitMQ Server
            %         host: localhost           # Hostname or IP of the RabbitMQ Server
            %         port: 5672                # Port the RabbitMQ Server runs on
            %         virtualhost: /            # RabbitMQ Virtual Host
            %         credentials: 
            %           username: guest         # RabbitMQ username
            %           password: guest         # RabbitMQ password
            %         exchange: amq.topic       # Exchange to work with on RabbitMQ
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
    end
    
end