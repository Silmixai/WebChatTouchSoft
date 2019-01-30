package com.mixail;

import javax.json.Json;
import javax.websocket.*;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;

@ClientEndpoint
public class ChatRoomClientEndpoint {
    private Session session = null;

    @OnOpen
    public void proccesOpen(Session session) {
        this.session = session;

    }

    public ChatRoomClientEndpoint() throws URISyntaxException, IOException, DeploymentException {
        URI uRI = new URI("ws://localhost:8080/client");
        ContainerProvider.getWebSocketContainer().connectToServer(this, uRI);

    }

    public void sendMessage(String message) throws IOException {
        if (!message.equals("/leave")) {
            session.getBasicRemote().sendText(message);
        }
    }


    @OnMessage
    public void proccesMessage(String message) {
        if (!message.equals("/leave")) {
            System.out.println(Json.createReader(new StringReader(message)).readObject().getString("message"));
        }
    }

    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
        System.out.println("Some Error");

    }

}
