/**
 * (c) 2025 MathWorks, Inc.
 */

package com.mathworks.messaging.utilities;

public class KeyManagerProperties {
    private String passphrase;
    public String getPassphrase() {
        return passphrase;
    }
    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }
    private String type = "PKCS12";
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    private String keystore;
    public String getKeystore() {
        return keystore;
    }
    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }

}
