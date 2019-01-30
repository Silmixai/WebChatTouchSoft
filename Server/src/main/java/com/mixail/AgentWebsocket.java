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
import java.io.StringReader;
import java.util.Base64;

@ServerEndpoint("/agent")
public class AgentWebsocket {

    private User agent;

    private UserService userService = UserService.getInstance();

    private static final Logger logger = LogManager.getLogger(AgentWebsocket.class);


    @OnOpen
    public void onOpen(Session session) {
        logger.info(" New connection, session: " + session);
        agent = new User();
        agent.setUserSession(session);
        agent.setStatus(Status.Registered);
    }


    @OnMessage
    public void handleMessage(String message,Session session) {

        String TypeofMessage = Json.createReader(new StringReader(message)).readObject().getString("TypeOfMessage");

        switch (TypeofMessage) {
            case "/register": {
                {
                    String agentPassword = Json.createReader(new StringReader(message)).readObject().getString("agentPassword");
                    byte[] decodedBytes = Base64.getDecoder().decode(agentPassword);
                    String decodedString = new String(decodedBytes);
                    agent.setPassword(decodedString);
                    System.out.println(decodedString);
                    String mes = Json.createReader(new StringReader(message)).readObject().getString("message");
                    String maxCountActiveChat = Json.createReader(new StringReader(message)).readObject().getString("maxCountActiveChat");
                    String typeofAgent = Json.createReader(new StringReader(message)).readObject().getString("TypeofAgent");
                    if (typeofAgent.equals("console")) {
                        agent.setConsoleAgent(true);
                    }
                    agent.setMaxCountActiveChat(Integer.parseInt(maxCountActiveChat));
                    userService.registrationUser(mes, agent);
                }
                break;
            }

            case "/signIn":
            {

                userService.signInAgent(message,agent);
                break;
            }

            case "/exit": {
                userService.exitAgent(agent);
                break;
            }

            case "/closeTab":
                userService.closeTabAgent(message, agent);
                break;

            case "new chatting": {

                userService.newChattingAgent(message, agent);
                break;
            }

            case "/leave":
                // liveUser();
                break;
        }
    }
}
