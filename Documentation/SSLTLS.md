# SSL/TLS Configuration

RabbitMQ supports [securing its (AMQP) channels using TLS](https://www.rabbitmq.com/docs/ssl). If working with such a TLS secured endpoint, the MATLAB RabbitMQ interface or MATLAB Production Server MessageBroker must explicitly be configured for this, in such cases you must:

1.  Provide a trust store containing the server certificate chain and/or trusted root CAs, and
2.  Optionally provide a keystore containing the client certificate and private key if your server requires client certificates.

Since the MATLAB RabbitMQ interfaces build on top of the RabbitMQ Java libraries, the trust store and key store need to be provided in Java compatible formats. Typically the trust store is in `JKS` format (with no predefined extension) and the keystore can be provided as `PKCS12` file (typically with the `.pfx` or `.p12` extension).

```{hint}
The server administrators who secured the RabbitMQ server with TLS in the first place, can likely help you with obtaining the correct certificates and keys, and in the right formats. 

If you are not an expert in this area, it is typically recommended to reach out to the experts within your company rather than try to obtain and/or convert the certificates on your own.

Nevertheless some further hints and tips are provided below.
```

## Trust Store

The Trust Store needs to contain the actual server certificate and/or chain and/or root CA(s) of the server which you want to connect to. The Trust Store is typically provided in `JKS` format. If you have a certificate (chain) in PEM-format instead, you can use Java's `keytool` to import it into a `JKS` Trust Store, for example:

```console
$ keytool -importcert -file certificate.pem -keystore ./mytruststore -storetype JKS
```

This will import the certificate(s) from `certificate.pem` into a new store in the current directory named `mytruststore`. `keytool` will ask for a new passphrase/password with which to secure this trust store.

## Key Store

If your server requires client certificates as well, you need to provide the client certificate and corresponding private key in the form of a `PKCS12` file; typically a file with `.p12` or `.pfx` extension. If you have the client certificate and corresponding key in two separate PEM-format files you will need to convert this to `PKCS12` format first, for example using the `openssl` commandline tool:

```console
$ openssl pkcs12 -export -out client.p12 -inkey client_key.pem -in client_certificate.crt -CAfile CA.crt -chain
```

This this the private key from the `client_key.pem` and certificate from `client_certificate.crt` and together with the CA certificate from `CA.crt` writes it out to `client.p12` in `PKCS12` format.

```{hint}
Depending on the version(s) of tooling used, the (relatively old) Java Runtime as included with MATLAB by default, may not be able to read the client private key from the PKCS12 file. In such cases you may need to configure MATLAB to work with a newer (OpenJDK) Java Runtime, see:

* <https://www.mathworks.com/support/requirements/openjdk.html>
* <https://www.mathworks.com/help/matlab/matlab_external/configure-your-system-to-use-java.html>
```