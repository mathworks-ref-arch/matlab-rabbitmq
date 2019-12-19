/**
 * Consumer Properties to store the settings for configuring this consumer
 * 
 * 		   (c) 2018 MathWorks, Inc.
 */
package com.mathworks.messaging.utilities;

public class ConsumerProperties {
	private Integer polltimeoutms;
	private String qos;
	/**
	 * @return the polltimeoutms
	 */
	public Integer getPolltimeoutms() {
		return polltimeoutms;
	}
	/**
	 * @param polltimeoutms the polltimeoutms to set
	 */
	public void setPolltimeoutms(Integer polltimeoutms) {
		this.polltimeoutms = polltimeoutms;
	}
	/**
	 * @return the qos
	 */
	public String getQos() {
		return qos;
	}
	/**
	 * @param qos the qos to set
	 */
	public void setQos(String qos) {
		this.qos = qos;
	}
	
}