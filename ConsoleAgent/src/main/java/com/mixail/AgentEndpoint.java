package com.mixail;


import javax.json.Json;
import javax.websocket.*;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;

@ClientEndpoint
public class AgentEndpoint {

    private Session session;
    private boolean isConnectedClient = false;

    @OnOpen
    public void processOpen(Session session) {
        this.session = session;
    }

    public AgentEndpoint() throws URISyntaxException, IOException, DeploymentException {
        URI uRI = new URI("ws://localhost:8080/agent");
        ContainerProvider.getWebSocketContainer().connectToServer(this, uRI);
    }

    public void sendRegisterMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendMessage(String message) {
        try {
            if (isConnectedClient) {
                session.getBasicRemote().sendText(message);
            } else
                if (Agent.isCorrectSignIn)
                ConsoleHelper.writeMessage("Please wait for the connected client prior to start the dialog. There are no connected clients now.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void processMessage(String message) {

        String typeOfMessage = Json.createReader(new StringReader(message)).readObject().getString("TypeOfMessage");

        switch (typeOfMessage) {
            case "Register":
                System.out.println("agent" + Json.createReader(new StringReader(message)).readObject().getString("agentName") + " is registered");
                break;
            case "Chatting": {
                String clientMessage = Json.createReader(new StringReader(message)).readObject().getString("message");
                String clientName = Json.createReader(new StringReader(message)).readObject().getString("clientName");
                System.out.println(clientName + ":" + clientMessage);
                break;
            }


            case "IncorrectName": {
                System.out.println("Wrong data, user with this name is not registered, please input '/sign in agent your_name' again!!");
                Agent.isRegisteredAgent = false;
                break;
            }

            case "IncorrectPassword":
            {
                System.out.println("Wrong password, please input '/sign in agent your_name' again!!!");
                Agent.isRegisteredAgent = false;
                break;
            }

            case "CorrectPassword":
            {
                Agent.isRegisteredAgent = true;
                Agent.isCorrectSignIn=true;
                System.out.println("Info: correct input");
                break;
            }

            case "Chatting Agent": {
                String clientMessage = Json.createReader(new StringReader(message)).readObject().getString("message");
                String agentName = Json.createReader(new StringReader(message)).readObject().getString("agentName");
                System.out.println(agentName + ":" + clientMessage);
                break;
            }

            case "ClientLeft": {
                String clientName = Json.createReader(new StringReader(message)).readObject().getString("clientName");
                System.out.println(clientName + " is left the chat");
                isConnectedClient = false;
                break;
            }
            case "find": {
                String clientName = Json.createReader(new StringReader(message)).readObject().getString("clientName");
                System.out.println(clientName + " is connected to you");
                isConnectedClient = true;
            }
            break;
        }
    }

    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
        System.out.println("Some Error");

    }

}
