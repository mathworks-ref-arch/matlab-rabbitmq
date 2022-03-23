classdef MessageReceivedData < event.EventData
    % MESSAGERECEIVEDDATA Used by rabbitmq.Consumer MessageReceived event
    % to return the received message.

    % Copyright 2022 The MathWorks, Inc.
    properties 
        message
    end

    methods
        function obj = MessageReceivedData(jMessage)
            obj.message = char(jMessage.getMessage);
        end
    end
end