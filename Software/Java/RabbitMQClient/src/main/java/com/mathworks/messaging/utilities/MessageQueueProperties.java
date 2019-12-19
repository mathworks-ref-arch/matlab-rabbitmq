/**
 * MessageQueue properties
 * 
 * 		   (c) 2019 MathWorks, Inc.
 */
package com.mathworks.messaging.utilities;

public class MessageQueueProperties
{
	
	private String protocol;
	private String host;
	private Integer port;
	private String virtualhost;
	private Credentials credentials;
	private String exchange;
	private String routingkey;
	private ConsumerProperties consumer;
	private String queueName;
	
	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}
	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	/**
	 * @return the credentials
	 */
	public Credentials getCredentials() {
		return credentials;
	}
	/**
	 * @param credentials the credentials to set
	 */
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
	/**
	 * @return the exchange
	 */
	public String getExchange() {
		return exchange;
	}
	/**
	 * @param exchange the exchange to set
	 */
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	/**
	 * @return the routingkey
	 */
	public String getRoutingkey() {
		return routingkey;
	}
	/**
	 * @param routingkey the routingkey to set
	 */
	public void setRoutingkey(String routingkey) {
		this.routingkey = routingkey;
	}
	/**
	 * @return the consumer
	 */
	public ConsumerProperties getConsumer() {
		return consumer;
	}
	/**
	 * @param consumer the consumer to set
	 */
	public void setConsumer(ConsumerProperties consumer) {
		this.consumer = consumer;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getVirtualhost() {
		return virtualhost;
	}
	public void setVirtualhost(String virtualhost) {
		this.virtualhost = virtualhost;
	}
	public String getQueueName() {
		return queueName;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	
}
