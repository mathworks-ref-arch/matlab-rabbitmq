classdef (SharedTestFixtures={dockerFixture}) testRabbitMQ < matlab.unittest.TestCase

    properties(TestParameter)
        config = {
            '/tmp/rabbit-config.yml',...
            '/tmp/rabbit-config-ssl.yml',...
            rabbitmq.ConnectorProperties(host=getHost()),...
            rabbitmq.ConnectorProperties( ...
                host=getHost(),...
                port=5671, ...
                sslcontext=rabbitmq.SSLContextProperties( ...
                    client=rabbitmq.KeyManagerProperties( ...
                        passphrase="supersecret", ...
                        keystore=fullfile(rabbitMQProjectRoot,'..','..','Test','certs','client.p12')  ...
                    ), ...
                    server=rabbitmq.TrustManagerProperties( ...
                        passphrase="rabbitstore", ...
                        truststore=fullfile(rabbitMQProjectRoot,'..','..','Test','certs','truststore') ...
                    ) ...
                ) ...
            )            
        }
    end

    methods (Test)
        function testSendAndPoll(testCase,config)
            % Create the producer
            producer = rabbitmq.Producer(config);
            testCase.verifyClass(producer,?rabbitmq.Producer);
            
            % Create the consumer
            consumer = rabbitmq.Consumer(config,false);
            testCase.verifyClass(consumer,?rabbitmq.Consumer);

            % Poll for a message, this should result in empty
            message = consumer.pollMessage();
            testCase.verifyEmpty(message);
            
            % Send a message
            producer.publish('test-topic','Hello World');
            % Poll again, this should now return a message (eventually),
            % try for 10 times with a 1 seconds delay each time, that
            % should be more than enough
            for i=1:10
                message = consumer.pollMessage();
                if ~isempty(message)
                    testCase.verifyEqual(message,'Hello World');
                    break
                end
                pause(1);
            end
        end

        function testSendAndEventBased(testCase,config)
            % Create the producer
            producer = rabbitmq.Producer(config);
            testCase.verifyClass(producer,?rabbitmq.Producer);
            
            % Create the consumer
            consumer = rabbitmq.Consumer(config,true);
            testCase.verifyClass(consumer,?rabbitmq.Consumer);
            % Add the listener
            addlistener(consumer,'MessageReceived',@onMessageReceived);
            
            % Prepare message variable
            message = [];

            % Send a message
            producer.publish('test-topic','Hello World');

            % Wait for the callback or a timeout (of 10 seconds)
            t = tic;
            while toc(t) < 10 && isempty(message)
                pause(0.1);
            end

            % Verify the message content
            testCase.verifyEqual(message,'Hello World');

            function onMessageReceived(src,evt)
                message = evt.message;
            end

        end

    end
end

function host = getHost()
    if isenv('DOCKER_HOST')
        host = getenv('DOCKER_HOST');
    else
        host = 'localhost';
    end
end