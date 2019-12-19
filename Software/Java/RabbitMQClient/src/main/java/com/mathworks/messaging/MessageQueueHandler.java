/**
 * MessageQueue Handler
 * 
 * 		   (c) 2019 MathWorks, Inc.
 */

package com.mathworks.messaging;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.mathworks.messaging.utilities.ConnectorProperties;
import com.mathworks.messaging.utilities.MPSClient;
import com.mathworks.utilities.OverrideHandler;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Envelope;

public class MessageQueueHandler {
	 private Connection connection = null;
	 private Channel channel = null;
	 private String QUEUE_NAME = "";
    
	 private String host = "";
	 private ConnectorProperties properties = null;
	 private ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
	 private ConnectionFactory factory = new ConnectionFactory();
	 private static final Logger LOG = LoggerFactory.getLogger(MessageQueueHandler.class);
	 
	 public void loadConfig(File fileName) throws Exception {
		 try {
			// Read the YAML file
	        	properties = mapper.readValue(fileName, ConnectorProperties.class);
	        	QUEUE_NAME = properties.getMessageQueue().getQueueName();
			 
		 }catch (Exception e) {
			 LOG.error("Unable to load config file", e);
				throw e; 
			}
	 }
	 
	public void setupConnectionFactoryFromConfig(File fileName) throws Exception {
		
		// set up Message Queue Connection Factory 
		try {
			loadConfig(fileName);
			//factory.setUsername("USERCREDENTIALS.local,"+ OverrideHandler.getOverride("MESSAGEQUEUE_CREDENTIALS_USERNAME",properties.getMessageQueue().getCredentials().getUsername()));
			//factory.setPassword((String) OverrideHandler.getOverride("MESSAGEQUEUE_CREDENTIALS_PASSWORD",properties.getMessageQueue().getCredentials().getPassword()));
			factory.setHost((String) OverrideHandler.getOverride("MESSAGEQUEUE_HOST",properties.getMessageQueue().getHost()));
			factory.setPort((int) OverrideHandler.getOverride("MESSAGEQUEUE_PORT",properties.getMessageQueue().getPort()));
			factory.setVirtualHost((String) OverrideHandler.getOverride("MESSAGEQUEUE_VIRTUAL_HOST", properties.getMessageQueue().getVirtualhost()));

		}catch (Exception e) {
			LOG.error("Unable to set up Message Queue connection factory from config file", e);
			throw e;
		}
	}
	
	public void setupMessageChannel () throws Exception {
		// set up Message Queue Channel
		try {
			connection = factory.newConnection();			
			QUEUE_NAME = properties.getMessageQueue().getQueueName();       	
        	channel = connection.createChannel();
        	// set up Exchange/Queue Binding                 
            channel.exchangeDeclare((String) OverrideHandler.getOverride("MESSAGEQUEUE_EXCHANGE", properties.getMessageQueue().getExchange()),"topic",true);
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);            
            channel.queueBind(QUEUE_NAME, (String) OverrideHandler.getOverride("MESSAGEQUEUE_EXCHANGE", properties.getMessageQueue().getExchange()), (String) OverrideHandler.getOverride("MESSAGEQUEUE_ROUTING_KEY",properties.getMessageQueue().getRoutingkey()));
				
		}catch (Exception e) {
			
			LOG.error("Unable to set up Message Queue Channel", e);
			throw e;
		}
	}
	
	public void setupMPS() throws Exception {
		try {
			
			// Configure the MPS connection
            MPSClient client = MPSClient.getInstance(); // Fetches a singleton
            try {
    			client.initialize(properties);
    		} catch (MalformedURLException e1) {
    			LOG.error("Unable to create a connection to MPS",e1);
    		}
		}catch (Exception e) {
			
			LOG.error("Unable to set up MATLAB Production Server", e);
			throw e;
		}
	}
	public void sendMessage(String routingKey, String message) throws UnsupportedEncodingException, IOException {
		try {
			channel.basicPublish(properties.getMessageQueue().getExchange(), routingKey, null, message.getBytes("UTF-8"));
	        System.out.println(" Message Sent to RabbitMQ: " + message );
		}catch (Exception e) {
			
			LOG.error("Unable to send message", e);
			throw e;
		}
	}
	
	public void receiveMessage() throws Exception {
		try {
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
	            String message = new String(delivery.getBody(), "UTF-8");
	            LOG.info("message to be received:"+ message);
	            MPSClient client = MPSClient.getInstance();
		        client.sendToMPS(message);
		        System.out.println("message received from RabbitMQ and forwarded to MPS:  "+ message);
	        };
	        
			channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
			
		}catch (Exception e) {
			
			LOG.error("Unable to receive message", e);
			throw e;
		}
	}
	
	/* This is another way to consume messages
	 * public void consumeMessage() throws Exception { try { Consumer consumerHandle
	 * = new DefaultConsumer(channel) {
	 * 
	 * @Override public void handleDelivery(String consumerTag, Envelope envelope,
	 * AMQP.BasicProperties properties, byte[] body) throws IOException { String
	 * message = new String(body, "UTF-8");
	 * 
	 * // Pass along to MPS LOG.info("message to be consumed:"+ message);
	 * 
	 * MPSClient client = MPSClient.getInstance(); client.sendToMPS(message);
	 * 
	 * } };
	 * 
	 * channel.basicConsume(this.QUEUE_NAME,consumerHandle);
	 * LOG.info(" [x] message consumed'" );
	 * 
	 * }catch(Exception e) { LOG.error("Unable to consume message", e); throw e; } }
	 */
	

}
