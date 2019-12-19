package com.mathworks.utilities;

//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.RenderingHints;
//import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.mathworks.messaging.utilities.ConnectorProperties;


/**
 * Class to display information strings.
 * 
 * (c) 2018 MathWorks, Inc.
 */

public class DisplayHelper {
	private static final Logger LOG = LoggerFactory.getLogger(DisplayHelper.class);

	public static void displayBanner() {
		
		LOG.info(" ");
		/* Create the text banner */
		StringBuilder sepBuilder = new StringBuilder();
		for (int x =0; x < 80; x++) {
			sepBuilder.append("=");
		}

		// Separator
		LOG.info(sepBuilder.toString());
		LOG.info("\r\n" +
		" __  __ _____   _____ _____                       _   _              _____                 _          \r\n" + 
		"|  \\/  |  __ \\ / ____|_   _|                     | | (_)            / ____|               (_)         \r\n" + 
		"| \\  / | |__) | (___   | |  _ __   __ _  ___  ___| |_ _  ___  _ __ | (___   ___ _ ____   ___  ___ ___ \r\n" + 
		"| |\\/| |  ___/ \\___ \\  | | | '_ \\ / _` |/ _ \\/ __| __| |/ _ \\| '_ \\ \\___ \\ / _ \\ '__\\ \\ / / |/ __/ _ \\\r\n" + 
		"| |  | | |     ____) |_| |_| | | | (_| |  __/\\__ \\ |_| | (_) | | | |____) |  __/ |   \\ V /| | (_|  __/\r\n" + 
		"|_|  |_|_|    |_____/|_____|_| |_|\\__, |\\___||___/\\__|_|\\___/|_| |_|_____/ \\___|_|    \\_/ |_|\\___\\___|\r\n" + 
		"                                   __/ |                                                              \r\n" + 
		"                                  |___/  ");
		LOG.info(sepBuilder.toString());
		
	}

	public static void displayHeader() {
		LOG.info("Lumada to MATLAB Production Server Connector");
		LOG.info("(c) 2018 MathWorks, Inc.");
	}
	
	
	public static void displayProperties(ConnectorProperties properties) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		String rawConfig = new String(mapper.writeValueAsString(properties));
		
		// redact all username/password information
		rawConfig = rawConfig.replaceAll("username:([^\\*].*)", "*****");
		rawConfig = rawConfig.replaceAll("password:([^\\*].*)", "*****");
		
		LOG.debug(rawConfig);
	}
	
}
