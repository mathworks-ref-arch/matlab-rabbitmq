/**
 * (c) 2025 MathWorks, Inc.
 */

package com.mathworks.messaging.utilities;

public class SSLContextProperties {
    private KeyManagerProperties client;
    public KeyManagerProperties getClient() {
        return client;
    }
    public void setClient(KeyManagerProperties client) {
        this.client = client;
    }
    private TrustManagerProperties server;
    public TrustManagerProperties getServer() {
        return server;
    }
    public void setServer(TrustManagerProperties server) {
        this.server = server;
    }
    private String protocol = "TLSv1.2";
    public String getProtocol() {
        return protocol;
    }
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

}
