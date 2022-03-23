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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.mathworks.messaging.utilities.ConnectorProperties;
import com.mathworks.messaging.utilities.MPSClient;
import com.mathworks.utilities.OverrideHandler;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.GetResponse;

public class MessageQueueHandler {

	private Connection connection = null;
	private Channel channel = null;
	private String QUEUE_NAME = "";

	private String host = "";
	private ConnectorProperties properties = null;
	private ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
	private ConnectionFactory factory = new ConnectionFactory();
	private static final Logger LOG = LoggerFactory.getLogger(MessageQueueHandler.class);

	private List<MessageReceivedListener> listeners = new ArrayList<MessageReceivedListener>();

	public synchronized void addMessageReceivedListener(MessageReceivedListener lis) {
		listeners.add(lis);
		LOG.info("MATLAB Listener added");
	}

	public synchronized void removeMessageReceivedListener(MessageReceivedListener lis) {
		listeners.remove(lis);
		LOG.info("MATLAB Listener removed");
	}

	public interface MessageReceivedListener extends java.util.EventListener {
		void messageReceivedEvent(MessageReceivedEvent event);
	}

	public class MessageReceivedEvent extends java.util.EventObject {
		private String message;

		public String getMessage() {
			return message;
		}

		MessageReceivedEvent(Object obj, String message) {
			super(obj);
			this.message = message;
		}
	}

	public void notifyMessageReceived(String message) {
		for (MessageReceivedListener l : listeners) {
			l.messageReceivedEvent(new MessageReceivedEvent(this, message));
		}
	}

	/**
	 * Loads configuration settings from a YAML-file
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	public void loadConfig(File fileName) throws Exception {
		try {
			// Read the YAML file
			properties = mapper.readValue(fileName, ConnectorProperties.class);
			QUEUE_NAME = properties.getMessageQueue().getQueueName();

		} catch (Exception e) {
			LOG.error("Unable to load config file", e);
			throw e;
		}
	}


	/**
	 * Sets configuration based on provided ConnectorProperties
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	public void setConfig(ConnectorProperties p) throws Exception {
		properties = p;
		QUEUE_NAME = properties.getMessageQueue().getQueueName();
	}

	private void setupConnectionFactory() {
		// set up Message Queue Connection Factory
		try {
			factory.setUsername((String) OverrideHandler.getOverride("MESSAGEQUEUE_CREDENTIALS_USERNAME",
					properties.getMessageQueue().getCredentials().getUsername()));
			factory.setPassword((String) OverrideHandler.getOverride("MESSAGEQUEUE_CREDENTIALS_PASSWORD",
					properties.getMessageQueue().getCredentials().getPassword()));
			factory.setHost(
					(String) OverrideHandler.getOverride("MESSAGEQUEUE_HOST", properties.getMessageQueue().getHost()));
			factory.setPort(
					(int) OverrideHandler.getOverride("MESSAGEQUEUE_PORT", properties.getMessageQueue().getPort()));
			factory.setVirtualHost((String) OverrideHandler.getOverride("MESSAGEQUEUE_VIRTUAL_HOST",
					properties.getMessageQueue().getVirtualhost()));

		} catch (Exception e) {
			LOG.error("Unable to set up Message Queue connection factory from config file", e);
			throw e;
		}		
	}

	/**
	 * Configures the connection factory based on provided ConnectorProperties
	 * @param p
	 * @throws Exception
	 */
	public void setupConnectionFactoryFromConfig(ConnectorProperties p) throws Exception {
		setConfig(p);
		setupConnectionFactory();
	}

	/**
	 * Configures the connection factory based on file provided as filename
	 * @param fileName
	 * @throws Exception
	 */
	public void setupConnectionFactoryFromConfig(String fileName) throws Exception {
		setupConnectionFactoryFromConfig(new File(fileName));
	}	

	/**
	 * Configures the connection factory based on ConnectorProperties
	 * @param fileName
	 * @throws Exception
	 */
	public void setupConnectionFactoryFromConfig(File fileName) throws Exception {
		loadConfig(fileName);
		setupConnectionFactory();
	}

	/**
	 * Creates a new connection based on previously loaded configuration 
	 * and previously configured connection factory.
	 * @throws Exception
	 */
	public void setupMessageChannel() throws Exception {
		// set up Message Queue Channel
		try {
			connection = factory.newConnection();
			QUEUE_NAME = properties.getMessageQueue().getQueueName();
			channel = connection.createChannel();
			// set up Exchange/Queue Binding
			channel.exchangeDeclare((String) OverrideHandler.getOverride("MESSAGEQUEUE_EXCHANGE",
					properties.getMessageQueue().getExchange()), "topic", true);
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			channel.queueBind(QUEUE_NAME,
					(String) OverrideHandler.getOverride("MESSAGEQUEUE_EXCHANGE",
							properties.getMessageQueue().getExchange()),
					(String) OverrideHandler.getOverride("MESSAGEQUEUE_ROUTING_KEY",
							properties.getMessageQueue().getRoutingkey()));

		} catch (Exception e) {

			LOG.error("Unable to set up Message Queue Channel", e);
			throw e;
		}
	}

	/**
	 * Initialize the MATLAB Production Server Java client based on
	 * previously loaded configuration.
	 * @throws Exception
	 */
	public void setupMPS() throws Exception {
		try {

			// Configure the MPS connection
			MPSClient client = MPSClient.getInstance(); // Fetches a singleton
			try {
				client.initialize(properties);
			} catch (MalformedURLException e1) {
				LOG.error("Unable to create a connection to MPS", e1);
			}
		} catch (Exception e) {

			LOG.error("Unable to set up MATLAB Production Server", e);
			throw e;
		}
	}

	/**
	 * Publishes a message on the previously configured channel
	 * @param routingKey
	 * @param message
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public void sendMessage(String routingKey, String message) throws UnsupportedEncodingException, IOException {
		try {
			channel.basicPublish(properties.getMessageQueue().getExchange(), routingKey, null,
					message.getBytes("UTF-8"));
			LOG.info("Message Sent to RabbitMQ.", message);
		} catch (Exception e) {

			LOG.error("Unable to send message", e);
			throw e;
		}
	}

	/**
	 * Tries to pull a message on the previously configured message
	 * @return null if there is no message or the actual message as
	 * string if a message was available.
	 * @throws IOException
	 */
	public String pollMessage() throws IOException {
		GetResponse r = channel.basicGet(QUEUE_NAME, true);
		if (r == null)
			return null;
		return new String(r.getBody(), "UTF-8");
	}

	/**
	 * Subscribes to messages. When a message is received a MessageReceived is raised
	 * with the message in the MessageReceivedEvent details.
	 * @throws Exception
	 */
	public void startReceivingMessages() throws Exception {
		try {
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "UTF-8");
				LOG.info("message received: " + message);
				notifyMessageReceived(message);
			};

			channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
			});

		} catch (Exception e) {

			LOG.error("Unable to receive message", e);
			throw e;
		}
	}

	/**
	 * Subscribes to messages. When a message is received it is forwarded 
	 * to the previously configured MPS function.
	 * @throws Exception
	 */
	public void receiveMessage() throws Exception {
		try {
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "UTF-8");
				LOG.info("Message received.", message);
				MPSClient client = MPSClient.getInstance();
				client.sendToMPS(message);
				LOG.info("Message forwarded to MPS.", message);
			};

			channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
			});

		} catch (Exception e) {

			LOG.error("Unable to receive message", e);
			throw e;
		}
	}

	/**
	 * To be called from MATLAB to close channel and connection when
	 * MATLAB wrapper objects are disposed.
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public void Dispose() throws IOException, TimeoutException {
		if (channel != null) {
			channel.close();
			channel = null;
		}
		if (connection != null) {
			connection.close();
			connection = null;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		Dispose();
		super.finalize();
	}

}
