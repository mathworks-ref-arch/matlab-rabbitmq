classdef ExchangeProperties < rabbitmq.object
    % EXCHANGEPROPERTIES Exhange related configuration used in
    % rabbitmq.ConnectorProperties.
    %
    % EXCHANGEPROPERTIES Properties:
    %
    %   name - Name of the exchange
    %          Type: string
    %          Default: "amq.topic"
    %
    %   type - Type of exchange
    %          Type: string
    %          Default: "topic"
    %
    %   create - Ensure exchange exists
    %            Type: logical
    %            Default: true
    %
    %   durable - Work with a durable exchange
    %             Type: logical
    %             Default: true
    %
    %   autoDelete - Work with an auto delete exchange
    %                Type: logical
    %                Default: false
    %
    %   internal - Work with an internal exchange
    %              Type: logical
    %              Default: false
    %
    % See Also CONNECTORPROPERTIES

    % Copyright 2023 The MathWorks, Inc.
    properties
        name
        type
        create
        durable
        autoDelete
        internal
    end
    properties (Dependent)
        arguments
    end    
    methods
        function obj = ExchangeProperties(options)
            arguments
                options.name string = "amq.topic"
                options.type string = "topic"
                options.create logical = true
                options.durable logical = true
                options.autoDelete logical = false
                options.internal logical = false                
            end
            obj.Handle = com.mathworks.messaging.utilities.ExchangeProperties();
            for p = string(fieldnames(options))'
                obj.(p) = options.(p);
            end
        end
        function set.name(obj,val)
            obj.Handle.setName(val);
            obj.name = val;
        end
        function set.type(obj,val)
            obj.Handle.setType(val);
            obj.type = val;
        end        
        function set.create(obj,val)
            obj.Handle.setCreate(val);
            obj.create = val;
        end
        function set.durable(obj,val)
            obj.Handle.setDurable(val);
            obj.durable = val;
        end
        function set.internal(obj,val)
            obj.Handle.setInternal(val);
            obj.internal = val;
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