#!/usr/bin/env bash
# Starts a RabbitMQ Server in a Docker container which can be used for testing
# The settings used by this server should be compatible with the default
# settings in mps.yaml and example.yaml.
set -x

docker run -d --rm \
    --name rabbitmq-for-matlab \
    -p 5672:5672 \
    -p 15672:15672 \
    rabbitmq:3-management

