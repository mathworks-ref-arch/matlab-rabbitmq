classdef Consumer < rabbitmq.object
% Consumer Allows consuming messages from a RabbitMQ Server
%
% Consumer Methods:
%
%   Consumer    - Constructor
%   pollMessage - Polls for a new message
%
% Consumer Events:
%
%   MessageReceived - Raised when a message is received   

% Copyright 2022-2023 The MathWorks, Inc.
    properties (Access=private)
        lh
    end

    events
        MessageReceived % Raised when a message is received   
    end

    methods        
        function obj = Consumer(config, eventBased)
            % CONSUMER Creates a Consumer instance. Either requires a
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
            %   There are two main Consumer approaches, an event based one
            %   or an approach where one can poll for new messages. The 
            %   event based approach is recommended but requires the
            %   RabbitMQ JAR-file to be loaded on the static class path. If
            %   it is loaded on the dynamic class path only the polling
            %   approach is available.
            %
            %   Examples:
            %
            %   Event Based Using ConnectorProperties:
            %   
            %       % Create ConnectorProperties with most settings set to
            %       % their default, but as an example override the routingkey
            %       configuration = rabbitmq.ConnectorProperties('routingkey','mykey');
            %       % Create the Consumer
            %       consumer = rabbitmq.Consumer(configuration,true);
            %       % Add a listener to the MessageReceived event
            %       addlistener(consumer,'MessageReceived',@myCallback);            
            %
            %   Event Based Using Configuration File:
            %
            %       % Create the consumer
            %       consumer = Consumer('myConfig.yaml', true);
            %       % Add a listener to the MessageReceived event
            %       addlistener(consumer,'MessageReceived',@myCallback);
            %
            %   Polling Consumer Using Configuration File:
            %
            %       % Create the consumer
            %       consumer = Consumer('myConfig.yaml', false);
            %       % Check for a message
            %       message = consumer.pollMessage();
            %
            %   See also ADDLISTENER, POLLMESSAGE, CONNECTORPROPERTIES

            % Although recommended as default do not work event based as
            % this requires the JAR on the static path which may not be the
            % case
            if nargin == 1
                eventBased = false;
            end

            % Create the Java MessageQueueHandler
            obj.Handle = com.mathworks.messaging.MessageQueueHandler();
            % Configure the MessageQueueHandler
            if isa(config,'rabbitmq.ConnectorProperties')
                config = config.Handle;
            end

            obj.Handle.setupConnectionFactoryFromConfig(config);
            obj.Handle.setupMessageChannel();
            
            if eventBased
                % Verify the package is on the static class path
                if obj.Handle.getClass().getClassLoader() ~= java.lang.ClassLoader.getSystemClassLoader()
                    error('Event based approach is only available when RabbitMQ package has been loaded on the static Java class path.');
                end
                % Add a listener and subscribe to the specified topic
                obj.lh = addlistener(obj.Handle,'MessageReceived',@obj.onMessageReceived);
                obj.Handle.startReceivingMessages();
            end
        end
        
        function response = pollMessage(obj)
            % POLLMESSAGE Polls for a new message.
            % Always returns immediately and does not wait for a message to
            % arrive. Returns emtpy when no message is available. Returns
            % the message as character array if a message has been
            % received.
            %
            %   message = consumer.pollMessage();
            response = char(obj.Handle.pollMessage);
        end
       
        function delete(obj)
            % DELETE Clean-up.
            % Closes the channel and connection to RabbitMQ releasing
            % resources on the server end.
            obj.Handle.Dispose();
        end

    end
    methods (Access=private)
        function onMessageReceived(obj,~,evt)
            % ONMESSAGERECEIVED Callback for the MessageReceived Java event
            % In turn raises a MATLAB MessageReceived event.
            notify(obj,'MessageReceived',rabbitmq.MessageReceivedData(evt));
        end
    end
    
end