#!/usr/bin/env bash

set -x

docker run -d --rm \
    --hostname l-shaped-rabbit \
    --name math-savvy-rabbit \
    -p 5672:5672 \
    -p 15672:15672 \
    -e RABBITMQ_DEFAULT_VHOST=rabbit \
    rabbitmq:3-management

