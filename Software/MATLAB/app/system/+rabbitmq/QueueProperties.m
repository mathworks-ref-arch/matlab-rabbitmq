classdef QueueProperties < rabbitmq.object
    % QUEUEPROPERTIES Queue related configuration used in
    % rabbitmq.ConnectorProperties.
    %
    % QUEUEPROPERTIES Properties:
    %
    %   name - Name of the queue
    %          Type: string
    %          Default: "RabbitMQ"
    %
    %   create - Ensure queue exists
    %            Type: logical
    %            Default: true
    %
    %   durable - Work with a durable queue
    %             Type: logical
    %             Default: false
    %
    %   exclusive - Work with an exclusive queue
    %              Type: logical
    %              Default: false
    %
    %   autoDelete - Work with an auto delete queue
    %                Type: logical
    %                Default: false
    %
    % See Also CONNECTORPROPERTIES
    
    % Copyright 2023 The MathWorks, Inc.
    properties
        name 
        create 
        durable
        exclusive
        autoDelete
    end
    properties (Dependent)
        arguments
    end
    methods
        function obj = QueueProperties(options)
            arguments
                options.name string = "RabbitMQ"
                options.create logical = true
                options.durable logical = false
                options.exclusive logical = false
                options.autoDelete logical = false
            end
            obj.Handle = com.mathworks.messaging.utilities.QueueProperties();
            for p = string(fieldnames(options))'
                obj.(p) = options.(p);
            end
        end

        function set.name(obj,val)
            obj.Handle.setName(val);
            obj.name = val;
        end
        function set.create(obj,val)
            obj.Handle.setCreate(val);
            obj.create = val;
        end
        function set.durable(obj,val)
            obj.Handle.setDurable(val);
            obj.durable = val;
        end
        function set.exclusive(obj,val)
            obj.Handle.setExclusive(val);
            obj.exclusive = val;
        end
        function set.autoDelete(obj,val)
            obj.Handle.setAutoDelete(val);
            obj.autoDelete = val;
        end
        function a = get.arguments(obj) 
            a = obj.Handle.getArguments();
        end

    end
end