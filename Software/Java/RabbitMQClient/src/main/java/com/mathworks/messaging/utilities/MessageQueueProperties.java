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
	private ExchangeProperties exchange;
	private String routingkey;
	private QueueProperties queue;
	
	public QueueProperties getQueue() {
		return queue;
	}
	public void setQueue(QueueProperties queue) {
		this.queue = queue;
	}
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
	public ExchangeProperties getExchange() {
		return exchange;
	}
	/**
	 * @param exchange the exchange to set
	 */
	public void setExchange(ExchangeProperties exchange) {
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
	
}
