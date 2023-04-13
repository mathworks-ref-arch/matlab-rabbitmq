package com.mathworks.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to override configuration settings
 * 
 * (c) 2018 MathWorks, Inc.
 */
public class OverrideHandler {
	// LOGGER for the class
	private static final Logger LOG = LoggerFactory.getLogger(OverrideHandler.class);
	
	public static Object getOverride(String key, Object defaultVal)
	{
		// Defensive initialization
		Object retVal = null;
		
		// Check if an ENV var is set
		String envOverride = System.getenv(key);
		if (envOverride!=null) {
			// Found override
			retVal = envOverride;
			LOG.debug("Detected configuration OVERRIDE for "+key);
		} else {
			retVal = defaultVal;
			LOG.debug("No OVERRIDE detected for "+key);
		}
				
		return retVal;
	}
}
