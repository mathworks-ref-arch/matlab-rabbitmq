/**
 * Connector Properties
 * 
 * 			 (c) 2018 MathWorks, Inc.
 */
package com.mathworks.messaging.utilities;

public class ConnectorProperties {
    private MPSProperties mps;
    private MessageQueueProperties messageQueue;

    public MPSProperties getMps() {
        return mps;
    }
    public void setMps(MPSProperties mps) {
        this.mps = mps;
    }
    public MessageQueueProperties getMessageQueue() {
        return messageQueue;
    }
    public void setMessageQueue(MessageQueueProperties messageQueue) {
        this.messageQueue = messageQueue;
    }
    
}
