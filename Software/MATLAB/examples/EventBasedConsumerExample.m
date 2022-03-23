function consumer = EventBasedConsumerExample
    % EVENTBASEDCONSUMEREXAMPLE Example which shows how a event based
    % RabbitMQ MATLAB consumer can be implemented

    % Copyright 2022 The MathWorks, Inc.    

    % Create the Consumer  with configuration file as input. Working with
    % rabbitmq.ConnectorProperties is supported here as well. Further set
    % eventBased = true to indicate to work with the event based approach 
    consumer = rabbitmq.Consumer( ...
        fullfile(rabbitMQProjectRoot,'config','example.yaml'), ...
        true);

    % Add a listener to the MessageReceived event
    addlistener(consumer,'MessageReceived',@onMessageReceived)


function onMessageReceived(~,message)
    % In the listener, as an example simply print the message
    fprintf('Message received: %s\n',message.message);