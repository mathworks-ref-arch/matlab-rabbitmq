classdef Credentials < rabbitmq.object
    % CREDENTIALS Credentials used in rabbitmq.ConnectorProperties.
    %
    % CREDENTIALS Properties:
    %
    %   username - username
    %              Type: string
    %              Default: "guest"
    %
    %   password - password
    %              Type: string
    %              Default: "guest"
    %
    % See Also CONNECTORPROPERTIES

    % Copyright 2023 The MathWorks, Inc.
    properties
        username 
        password
    end
    methods
        function obj = Credentials(options)
            arguments
                options.username string = "guest"
                options.password string = "guest"
            end
            obj.Handle = com.mathworks.messaging.utilities.Credentials();
            for p = string(fieldnames(options))'
                obj.(p) = options.(p);
            end
        end

        function set.username(obj,val)
            obj.Handle.setUsername(val);
            obj.username = val;
        end

        function set.password(obj,val)
            obj.Handle.setPassword(val);
            obj.password = val;
        end        
    end
end