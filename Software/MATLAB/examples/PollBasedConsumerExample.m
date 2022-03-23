function PollBasedConsumerExample
    % POLLBASEDCONSUMEREXAMPLE Example which shows how a polling based
    % RabbitMQ MATLAB consumer can be implemented
    
    % Copyright 2022 The MathWorks, Inc.    

    % Create the Consumer  with configuration file as input working with
    % rabbitmq.ConnectorProperties is supported here as well. Further set
    % eventBased = false to indicate to work with the polling approach
    consumer = rabbitmq.Consumer( ...
        fullfile(rabbitMQProjectRoot,'config','example.yaml'), ...
        false);
    % Now actually start polling
    
    % In this example receive up to 3 messages then stop
    numReceived = 0;
    fprintf('Polling for messages...');
    % As long as we have not received three messages yet
    while numReceived < 3
        % Poll for a message
        message = consumer.pollMessage();
        if ~isempty(message) % If a message was received 
            % Display it
            fprintf('\nMessage Received: %s\n',message);
            % Increase the counter
            numReceived = numReceived + 1;
            fprintf('Polling for messages...');
        end
        fprintf('.')
        % Pause for a second before polling again
        pause(1);
    end
    % After 3 messages have been received
    fprintf('stopped.\n');