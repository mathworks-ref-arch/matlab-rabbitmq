classdef SSLContextProperties < rabbitmq.object
    % SSLContextProperties Queue related configuration used in
    % rabbitmq.ConnectorProperties.
    %

    % See Also CONNECTORPROPERTIES
    
    % Copyright 2023 The MathWorks, Inc.
    properties
        server rabbitmq.TrustManagerProperties
        client rabbitmq.KeyManagerProperties
        protocol string
    end
    properties (Dependent)
        arguments
    end
    methods
        function obj = SSLContextProperties(options)
            arguments
                options.server 
                options.client 
                options.protocol string = "TLSv1.2"
            end
            obj.Handle = com.mathworks.messaging.utilities.SSLContextProperties();
            for p = string(fieldnames(options))'
                obj.(p) = options.(p);
            end
        end

        function set.server(obj,val)
            obj.Handle.setServer(val.Handle);
            obj.server = val;
        end
        function set.client(obj,val)
            obj.Handle.setClient(val.Handle);
            obj.client = val;
        end
        function set.protocol(obj,val)
            obj.Handle.setProtocol(val);
            obj.protocol = val;
        end        
    end
end