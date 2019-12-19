package com.mathworks.messaging.utilities;
/**
 * Main class that communicates to MATLAB Production Server
 * 
 *		   (c) 2018 MathWorks, Inc.
 */

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import com.mathworks.mps.client.MWClient;
import com.mathworks.mps.client.MWHttpClient;
import com.mathworks.mps.client.MWInvokable;
import com.mathworks.utilities.OverrideHandler;
import com.mathworks.messaging.utilities.ConnectorProperties;
import com.mathworks.messaging.utilities.MPSProperties;
import com.mathworks.mps.client.MATLABException;;

public class MPSClient {
	private MWClient clientHandle= null;
	private MWInvokable clientProxy = null;
	private URL archiveURL = null;
	private String endPoint = null;
	
	// static variable single_instance aka Singleton
    private static MPSClient single_instance = null;
    
    // LOGGER for the class
 	private static final Logger LOG = LoggerFactory.getLogger(MPSClient.class);
 	
	/* Constructor */
	public void initialize(ConnectorProperties properties) throws MalformedURLException
	{
		/* Fetch the properties that define the MPS endpoint */
		MPSProperties mps = properties.getMps();
		
		/* Create a handle to MPS*/
		this.clientHandle = new MWHttpClient();
		this.archiveURL = new URL((String) OverrideHandler.getOverride("MPS_PROTOCOL", mps.getProtocol())+
				"://"+
				(String) OverrideHandler.getOverride("MPS_HOST", mps.getHost())+
				":"+
				String.valueOf(OverrideHandler.getOverride("MPS_PORT", mps.getPort()))+
				"/"+
				(String) OverrideHandler.getOverride("MPS_ARCHIVE", mps.getArchive()));
		this.clientProxy = this.clientHandle.createComponentProxy(this.archiveURL);
		
		/* Store the function to be called */
		this.endPoint = mps.getFunction();
		
		
	}
	
	/* Method to send the data to MPS */
	public void sendToMPS(String message)
	{
		/* Invoke the MATLAB function */
		try {
			
			Object[] outputs = this.clientProxy.invoke(this.endPoint, 0, Object[].class, message);
					
			/* Process the outputs */
			//System.out.println("MPS returned message:"+ outputs);
			
		} catch (Throwable e) {
			LOG.error("Unable to call MATLAB Production Server", e);
		}
	}
	
	// static method to create instance of Singleton class
    public static MPSClient getInstance()
    {
        if (single_instance == null)
            single_instance = new MPSClient();
 
        return single_instance;
    }
}
