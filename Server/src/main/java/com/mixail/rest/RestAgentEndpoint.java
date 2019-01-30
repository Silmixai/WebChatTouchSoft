package com.mixail.rest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@ClientEndpoint
public class RestAgentEndpoint {
    private Session session = null;
    private List<String> messages = new ArrayList<>();

    private static final Logger logger = LogManager.getLogger(RestAgentEndpoint.class);
    public List<String> getMessages() {
        return messages;
    }

    public void clearMessageHistory()
    {
        messages.clear();
    }

    @OnOpen
    public void processOpen(Session session) {
        this.session = session;
        logger.info("connected Rest agent");

    }

    public RestAgentEndpoint() throws URISyntaxException, IOException, DeploymentException {
        URI uRI = new URI("ws://localhost:8080/agent");
        ContainerProvider.getWebSocketContainer().connectToServer(this, uRI);
    }

    @OnMessage
    public void processMessage(String message) {
        messages.add(message);
        System.out.println(message);
    }

    public void sendMessage(String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }

}
