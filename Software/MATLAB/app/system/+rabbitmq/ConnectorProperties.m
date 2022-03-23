classdef ConnectorProperties < rabbitmq.object
    % CONNECTORPROPERTIES Configures various settings for RabbitMQ Producer
    % and Consumer classes.
    %
    % The Constructor takes a number of Name-Value pairs as input. All
    % options are optional, if not specified their default value is used.
    %
    % Name-Value Pairs:
    %
    %   Hostname or IP of the RabbitMQ Server
    %       'host' = 'localhost'
    %
    %   Port the RabbitMQ Server runs on
    %       'port' = 5672
    %
    %   RabbitMQ Virtual Host
    %       'virtualhost' = '/'
    %
    %   Exchange to work with on RabbitMQ
    %       'exchange' = 'amq.topic'
    %
    %   Name of the Queue on RabbitMQ Server
    %       'queuename' = 'RabbitMQ'
    %
    %   RabbitMQ username
    %       'username' = 'guest'
    %
    %   RabbitMQ password
    %       'password' = 'guest'
    %
    %   Routing key to subscribe to or poll on - only used for Consumer,
    %   when working with Producer the routing key is specified on a per
    %   message basis when publishing a message
    %       'routingkey' = 'test-topic'
    %
    % Examples:
    %
    %   % Create a configuration with mostly default properties but
    %   % override host and routingkey
    %   configuration = rabbitmq.ConnectorProperties(...
    %       'host','myhost',...
    %       'routingkey,'my-key');
    %
    %   See Also CONSUMER, PRODUCER
    
    % Copyright 2020-2022 The MathWorks, Inc.
    properties
        host
        port
        virtualhost
        username
        exchange
        routingkey
        queuename
    end
    properties (GetAccess=private)
        password
    end
    
    methods
        function obj = ConnectorProperties(varargin)

            p = inputParser;
            p.FunctionName = 'ConnectorProperties';
            scalarText = @(x)validateattributes(x,{'char','string'},{'scalartext'});
            p.addParameter('host','localhost',scalarText);
            p.addParameter('port',5672,@(x)validateattributes(x,{'numeric'},{'scalar','integer'}));
            p.addParameter('virtualhost','/',scalarText);
            p.addParameter('username','guest',scalarText);
            p.addParameter('password','guest',scalarText);
            p.addParameter('exchange','amq.topic',scalarText);
            p.addParameter('routingkey','test-topic',scalarText);
            p.addParameter('queuename','RabbitMQ',scalarText);
            p.parse(varargin{:});

            options = p.Results;

            cp = com.mathworks.messaging.utilities.ConnectorProperties;
            mp = com.mathworks.messaging.utilities.MessageQueueProperties;
            cr = com.mathworks.messaging.utilities.Credentials;
            mp.setCredentials(cr);
            cp.setMessageQueue(mp);
            obj.Handle = cp;

            obj.host = options.host;
            obj.port = options.port;
            obj.virtualhost = options.virtualhost;
            obj.username = options.username;
            obj.password = options.password;
            obj.exchange = options.exchange;
            obj.routingkey = options.routingkey;
            obj.queuename = options.queuename;

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
        function set.username(obj,val)
            obj.Handle.getMessageQueue.getCredentials.setUsername(val);
            obj.username = val;
        end
        function set.password(obj,val)
            obj.Handle.getMessageQueue.getCredentials.setPassword(val);
            obj.password = val;
        end
        function set.exchange(obj,val)
            obj.Handle.getMessageQueue.setExchange(val);
            obj.exchange = val;
        end
        function set.routingkey(obj,val)
            obj.Handle.getMessageQueue.setRoutingkey(val);
            obj.routingkey = val;
        end
        function set.queuename(obj,val)
            obj.Handle.getMessageQueue.setQueueName(val);
            obj.queuename = val;
        end

    end
end