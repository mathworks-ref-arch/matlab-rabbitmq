/**
 * MessageBroker Class
 * 
 * 		   (c) 2019-2022 MathWorks, Inc.
 */

package com.mathworks.messaging;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageBroker {

	 
	private static final Logger LOG = LoggerFactory.getLogger(MessageBroker.class);
	
	public static void main(String[] argv) {
		
		MessageQueueHandler mqHandler = null;
		try {
			// Create MessageQueueHandler object, set up RabbitMQ and MPS connection, then consume message
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
