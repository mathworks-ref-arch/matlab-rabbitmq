function MPSreceive(message)
   % MPSRECEIVE Example function which can be deployed to MATLAB Production
   % Server. It simply prints the received the message to the Command
   % Window when testing inside MATLAB and to the main.log when deployed to
   % an actual MATLAB Production Server Instance.
   
   % Copyright 2022 The MathWorks, Inc.    

   fprintf('Message Received from RabbitMQ: %s\n',message);
    
    
end
