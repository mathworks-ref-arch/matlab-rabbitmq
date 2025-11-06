classdef TrustManagerProperties < rabbitmq.object
    % TrustManagerProperties Queue related configuration used in
    % rabbitmq.SSLContextProperties.
    %

    % See Also SSLContextProperties
    
    % Copyright 2023 The MathWorks, Inc.
    properties
        passphrase string
        truststore string
        type string
        hostnameVerification logical
    end
    properties (Dependent)
        arguments
    end
    methods
        function obj = TrustManagerProperties(options)
            arguments
                options.passphrase string
                options.truststore string
                options.type string = "JKS"
                options.hostnameVerification logical = true;
            end
            obj.Handle = com.mathworks.messaging.utilities.TrustManagerProperties();
            for p = string(fieldnames(options))'
                obj.(p) = options.(p);
            end
        end

        function set.passphrase(obj,val)
            obj.Handle.setPassphrase(val);
            obj.passphrase = val;
        end
        function set.truststore(obj,val)
            obj.Handle.setTruststore(val);
            obj.truststore = val;
        end
        function set.type(obj,val)
            obj.Handle.setType(val);
            obj.type = val;
        end            
        function set.hostnameVerification(obj,val)
            obj.Handle.setHostnameVerification(val);
            obj.hostnameVerification = val;
        end                 
    end
end