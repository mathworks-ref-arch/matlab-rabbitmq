package com.mathworks.messaging;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

// Copyright 2022-2025 The MathWorks

public class TestMessageQueue {
    private MockWebServer server;

    @Before
    public void start() throws IOException {
        // Mock MPS
        server = new MockWebServer();
        server.enqueue(new MockResponse().setResponseCode(200));
        server.start(9910);
        server.url("/");
    }

    @After
    public void stop() throws IOException {
        server.shutdown();
    }

    @Test
    public void testMessageQueueHandler() throws Exception {

        assertEquals(9910,server.getPort());

        // Create the MessageQueueHandler
        MessageQueueHandler mqHandler = new MessageQueueHandler();
        // Configure in the same way as in MessageBroker
        mqHandler.setupConnectionFactoryFromConfig(new File("src/main/resources/mps.yaml"));
        mqHandler.setupMessageChannel();
        mqHandler.setupMPS();
        mqHandler.receiveMessage();

        // Send a message
        mqHandler.sendMessage("test-topic", "hello");

        // Verify that "MPS" was indeed called on the configured end-point
        // within a reasonable amount of time, at least not block forever
        RecordedRequest r = server.takeRequest(10,TimeUnit.SECONDS);
        assertEquals("/demo/MPSreceive",r.getPath());

        // Clean up explicitly
        mqHandler.Dispose();
    }

    @Test
    public void testMessageQueueHandlerSSL() throws Exception {

        assertEquals(9910,server.getPort());

        // Create the MessageQueueHandler
        MessageQueueHandler mqHandler = new MessageQueueHandler();
        // Configure in the same way as in MessageBroker
        mqHandler.setupConnectionFactoryFromConfig(new File("src/main/resources/mps_ssl.yaml"));
        mqHandler.setupMessageChannel();
        mqHandler.setupMPS();
        mqHandler.receiveMessage();

        // Send a message
        mqHandler.sendMessage("test-topic", "hello");

        // Verify that "MPS" was indeed called on the configured end-point
        // within a reasonable amount of time, at least not block forever
        RecordedRequest r = server.takeRequest(10,TimeUnit.SECONDS);
        assertEquals("/demo/MPSreceive",r.getPath());

        // Clean up explicitly
        mqHandler.Dispose();
    }    
}