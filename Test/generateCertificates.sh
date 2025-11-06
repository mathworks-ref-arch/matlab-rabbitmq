#!/bin/bash

# Set HOSTNAME to localhost by default by allow to be overridden using DOCKER_HOST environment variable
HOSTNAME=${DOCKER_HOST:-localhost}

# Remove old certificates
rm -rf certs
# Create new ones
mkdir certs
cd certs

# Create the server self-signed certificate
openssl req -newkey rsa:2048 -noenc -keyout server.pem -x509 -days 365 -subj "/CN=${HOSTNAME}/" -addext "basicConstraints=critical,CA:FALSE" -addext "keyUsage=digitalSignature,keyEncipherment" -addext "extendedKeyUsage=serverAuth" -addext "subjectAltName=DNS:${HOSTNAME}" -out server.crt

# Create JKS trust store with server cert
keytool -importcert -file server.crt -keystore truststore -storetype JKS -storepass rabbitstore -noprompt


# Create the CA
openssl req -newkey rsa:2048 -noenc -keyout CA.pem -x509 -days 365 -subj "/CN=MyRabbitMQServer/" -addext "basicConstraints=critical,CA:TRUE" -addext "keyUsage=keyCertSign,cRLSign" -out CA.crt
# Create a client certificate using CA
openssl req -newkey rsa:2048 -noenc -keyout client.pem -x509 -CA CA.crt -CAkey CA.pem -days 365 -subj "/CN=MyMATLABTestClient/" -addext "basicConstraints=critical,CA:FALSE" -addext "keyUsage=digitalSignature,keyEncipherment" -addext "extendedKeyUsage=clientAuth" -out client.crt

# Create PKCS12 key store with client certificate and private key
openssl pkcs12 -export -out client.p12 -inkey client.pem -in client.crt -CAfile CA.crt -chain -passout "pass:supersecret"

# Allow certificates and keys to be read by anyone such that the rabbitmq user inside the Docker container
# can read them as well. Since these are temporary self-signed certificates used by the unit tests only anyway,
# this is okay. DO NOT do this with private keys in production environments!
chmod a+r *