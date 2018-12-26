package com.mixail;

import com.mixail.model.Status;
import com.mixail.model.User;
import com.mixail.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.json.Json;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.StringReader;

@ServerEndpoint("/client")
public class ClientWebsocket {

    private User client;

    private UserService userService = UserService.getInstance();

    private static final Logger logger = LogManager.getLogger(ClientWebsocket.class);


    @OnOpen
    public void onOpen(Session session) {
        logger.info(" New connection, client session : " + session);
        client = new User();
        client.setUserSession(session);
        client.setStatus(Status.Registered);
    }


    @OnMessage
    public void handleMessage(String message) throws IOException {

        String mes = Json.createReader(new StringReader(message)).readObject().getString("message");
        String TypeofMessage = Json.createReader(new StringReader(message)).readObject().getString("TypeOfMessage");

        switch (TypeofMessage) {
            case "/register": {
                {
                    userService.registrationUser(mes, client);
                }
                break;
            }
            case "/exit":

                userService.clientExit(client);
                break;

            case "new chatting": {
                userService.newChattingClient(mes, client);
                break;
            }
            case "/leave":
                userService.leaveClient(client);
                break;
        }
    }
}
