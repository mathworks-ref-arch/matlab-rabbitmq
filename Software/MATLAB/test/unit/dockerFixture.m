classdef dockerFixture < matlab.unittest.fixtures.Fixture
    properties (Access=private)
        container
    end
    methods
        function setup(fixture)
            cfg = fileread(fullfile(rabbitMQProjectRoot,'config','example.yaml'));
            cfg = strrep(cfg,'localhost',getHost);
            f = fopen('/tmp/rabbit-config.yml','w');
            fwrite(f,cfg);
            fclose(f);

            cfg = fileread(fullfile(rabbitMQProjectRoot,'config','example_ssl.yaml'));
            cfg = strrep(cfg,'localhost',getHost);
            cfg = strrep(cfg,'Test/',fullfile(rabbitMQProjectRoot,'..','..','Test/'));
            f = fopen('/tmp/rabbit-config-ssl.yml','w');
            fwrite(f,cfg);
            fclose(f);


        end
        function teardown(fixture)

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