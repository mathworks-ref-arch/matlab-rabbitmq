function RabbitMQuser = MPSreceive(inputname)

   % Copyright 2019 The MathWorks, Inc.    


   % This the test file for RabbitMQ invoke a MATLAB function.  


   % Limit inputs to char or string. Convert if necessary.
    if ~(ischar(inputname) || isstring(inputname))
        inputname = num2str(inputname);
    end
    
    
    
    % Receive message
    if exist('inputname','var')
        
        % generate messages
        conf = strcat('MPS received confirmation from RabbitMQ --',' ', inputname);
        RabbitMQuser = strcat('Your message received by MPS --',' ', inputname);
        
        % display a message here
        disp(conf);
        
        % enforce that the outgoing message is a string
        %user = string(user);
        
    end
    
    
end
