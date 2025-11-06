classdef ConnectorProperties < rabbitmq.object
    % CONNECTORPROPERTIES Configures various settings for RabbitMQ Producer
    % and Consumer classes.
    %
    % CONNECTORPROPERTIES Properties:
    %
    %   host - Hostname or IP of the RabbitMQ Server 
    %          Type: String
    %          Default: "localhost"
    %
    %   port - Port the RabbitMQ Server runs on  
    %          Type: int32
    %          Default: 5672
    %
    %   virtualhost - RabbitMQ Virtual Host
    %                 Type: String
    %                 Default: "/"
    %
    %   credentials - RabbitMQ server credentials
    %                 Type: rabbitmq.Credentials
    %
    %   exchange - Exchange to work with on RabbitMQ
    %              Type: rabbitmq.ExchangeProperties
    %
    %   routingkey - Routing key to subscribe to or poll on - only used for
    %                   Consumer, when working with Producer the routing
    %                   key is specified on a per message basis when
    %                   publishing a message queue
    %                Type: string
    % Examples:
    %
    %   % Create a configuration with mostly default properties but
    %   % override host and routingkey
    %   configuration = rabbitmq.ConnectorProperties(...
    %       'host','myhost',...
    %       'routingkey,'my-key');
    %
    %   See Also rabbitmq.ExchangeProperties, rabbitmq.QueueProperties,
    %       rabbitmq.Credentials, rabbitmq.Consumer, rabbitmq.Producer
    
    % Copyright 2020-2023 The MathWorks, Inc.
    properties
        host 
        port 
        virtualhost 
        credentials 
        exchange 
        routingkey 
        queue
        sslcontext rabbitmq.SSLContextProperties
    end
   
    methods
        function obj = ConnectorProperties(options)
            arguments
                options.host string = "localhost"
                options.port int32 = 5672
                options.virtualhost string = "/"
                options.credentials = rabbitmq.Credentials
                options.exchange = rabbitmq.ExchangeProperties
                options.routingkey string = "test-topic"
                options.queue = rabbitmq.QueueProperties
                options.sslcontext 
            end

            cp = com.mathworks.messaging.utilities.ConnectorProperties;
            mp = com.mathworks.messaging.utilities.MessageQueueProperties;
            cp.setMessageQueue(mp);

            obj.Handle = cp;

            for p = string(fieldnames(options))'
                obj.(p) = options.(p);
            end
        end

        function set.host(obj,val)
            obj.Handle.getMessageQueue.setHost(val);
            obj.host = val;
        end
        function set.port(obj,val)
            obj.Handle.getMessageQueue.setPort(java.lang.Integer(val));
            obj.port = val;
        end
        function set.virtualhost(obj,val)
            obj.Handle.getMessageQueue.setVirtualhost(val);
            obj.virtualhost = val;
        end
        function set.credentials(obj,val)
            obj.Handle.getMessageQueue.setCredentials(val.Handle);
            obj.credentials = val;
        end
        function set.exchange(obj,val)
            obj.Handle.getMessageQueue.setExchange(val.Handle);
            obj.exchange = val;
        end
        function set.routingkey(obj,val)
            obj.Handle.getMessageQueue.setRoutingkey(val);
            obj.routingkey = val;
        end
        function set.queue(obj,val)
            obj.Handle.getMessageQueue.setQueue(val.Handle);
            obj.queue = val;
        end
        function set.sslcontext(obj,val)
            obj.Handle.getMessageQueue.setSslcontext(val.Handle);
            obj.sslcontext = val;
        end

    end
end