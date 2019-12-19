/**
 * MessageQueue Receiver
 * 
 * 		   (c) 2019 MathWorks, Inc.
 */

package com.mathworks.messaging;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mathworks.messaging.MessageQueueHandler;

public class MessageReceiver {

	 
	private static final Logger LOG = LoggerFactory.getLogger(MessageReceiver.class);
	
	public static void main(String[] argv) {
		
		MessageQueueHandler mqHandler = null;
		try {
			
			String configFile = argv[0];
			System.out.println(" Waiting for messages. To exit press CTRL+C");			
			mqHandler = new MessageQueueHandler();
			mqHandler.setupConnectionFactoryFromConfig(new File(configFile));
			mqHandler.setupMessageChannel();
			mqHandler.setupMPS();
			mqHandler.receiveMessage();
			
			
		}catch (Exception e) {
			LOG.error("receiving message has error.", e);
						
		}
	}
}
