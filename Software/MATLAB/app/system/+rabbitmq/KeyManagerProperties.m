classdef KeyManagerProperties < rabbitmq.object
    % KeyManagerProperties Queue related configuration used in
    % rabbitmq.SSLContextProperties.
    %

    % See Also SSLContextProperties
    
    % Copyright 2023 The MathWorks, Inc.
    properties
        passphrase string
        keystore string
        type string
    end
    properties (Dependent)
        arguments
    end
    methods
        function obj = KeyManagerProperties(options)
            arguments
                options.passphrase string
                options.keystore string
                options.type string = "PKCS12"
            end
            obj.Handle = com.mathworks.messaging.utilities.KeyManagerProperties();
            for p = string(fieldnames(options))'
                obj.(p) = options.(p);
            end
        end

        function set.passphrase(obj,val)
            obj.Handle.setPassphrase(val);
            obj.passphrase = val;
        end
        function set.keystore(obj,val)
            obj.Handle.setKeystore(val);
            obj.keystore = val;
        end
        function set.type(obj,val)
            obj.Handle.setType(val);
            obj.type = val;
        end        
    end
end