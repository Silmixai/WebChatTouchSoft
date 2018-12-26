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
public class RestClientEndpoint {

    private static final Logger logger = LogManager.getLogger(RestClientEndpoint.class);
    private List<String> messages = new ArrayList<>();

    public List<String> getMessages() {
        return messages;
    }

    public void clearMessageHistory()
    {
        messages.clear();
    }


    private Session session = null;

    public RestClientEndpoint() throws URISyntaxException, IOException, DeploymentException {
        URI uRI = new URI("ws://localhost:8080/client");
        ContainerProvider.getWebSocketContainer().connectToServer(this, uRI);
    }

    @OnOpen
    public void processOpen(Session session) {
        this.session = session;
        logger.info(" New Rest  client connection, session: " + session);

    }

    @OnMessage
    public void processMessage(String message) {
        messages.add(message);
    }

    public void sendMessage(String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        logger.error(" close connection: " + session);
    }

}
