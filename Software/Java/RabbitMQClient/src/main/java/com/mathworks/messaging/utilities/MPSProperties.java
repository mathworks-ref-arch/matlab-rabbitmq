/**
 * MPS Connection and MPS-Client properties
 * 
 * 		   (c) 2018 MathWorks, Inc.
*/
package com.mathworks.messaging.utilities;

public class MPSProperties
{
	private String protocol;
	private String host;
	private Integer port;
	private String archive;
	private String function;
	private Boolean debug;
	private Integer timeoutms;
	/**
	 * @return the port
	 */
	public Integer getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(Integer port) {
		this.port = port;
	}
	/**
	 * @return the archive
	 */
	public String getArchive() {
		return archive;
	}
	/**
	 * @param archive the archive to set
	 */
	public void setArchive(String archive) {
		this.archive = archive;
	}
	/**
	 * @return the function
	 */
	public String getFunction() {
		return function;
	}
	/**
	 * @param function the function to set
	 */
	public void setFunction(String function) {
		this.function = function;
	}
	/**
	 * @return the debug
	 */
	public Boolean getDebug() {
		return debug;
	}
	/**
	 * @param debug the debug to set
	 */
	public void setDebug(Boolean debug) {
		this.debug = debug;
	}
	/**
	 * @return the timeoutms
	 */
	public Integer getTimeoutms() {
		return timeoutms;
	}
	/**
	 * @param timeoutms the timeoutms to set
	 */
	public void setTimeoutms(Integer timeoutms) {
		this.timeoutms = timeoutms;
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
	 * @return the host
	 */
	public String getHost() {
		return host;
	}
	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}
	
}
