/**
 * MessageQueue Sender
 * 
 * 		   (c) 2019 MathWorks, Inc.
 */

package com.mathworks.messaging;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageSender {

	private static final Logger LOG = LoggerFactory.getLogger(MessageSender.class);
	
	public static void main(String[] argv) {
		
	    MessageQueueHandler mqHandler = null;
		try {
			// Create MessageQueueHandler object, set up RabbitMQ connection and send message
			String configFile = argv[0];
			mqHandler = new MessageQueueHandler();
			mqHandler.setupConnectionFactoryFromConfig(new File(configFile));
			mqHandler.setupMessageChannel();
			System.out.println(" To stop sending messages, press CTRL+C");	
			while(true) {
				String message = System.console().readLine("Message:");
				String routingKey = System.console().readLine("Message's Routing Key:");
				mqHandler.sendMessage(routingKey, message);
			}
			
			
		}catch (Exception e) {
			LOG.error("Error sending message", e);
						
		}
	}
}
