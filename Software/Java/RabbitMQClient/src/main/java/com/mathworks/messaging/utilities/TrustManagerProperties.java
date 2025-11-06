/**
 * (c) 2025 MathWorks, Inc.
 */
package com.mathworks.messaging.utilities;

public class TrustManagerProperties {
    private String passphrase;
    public String getPassphrase() {
        return passphrase;
    }
    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }
    private String type = "JKS";
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    private String truststore;
    public String getTruststore() {
        return truststore;
    }
    public void setTruststore(String truststore) {
        this.truststore = truststore;
    }
    private boolean hostnameVerification = true;
    public boolean isHostnameVerification() {
        return hostnameVerification;
    }
    public void setHostnameVerification(boolean hostnameVerification) {
        this.hostnameVerification = hostnameVerification;
    }

}
