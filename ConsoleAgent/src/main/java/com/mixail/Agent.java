package com.mixail;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.websocket.DeploymentException;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;

public class Agent {
    public static void main(String[] args) throws DeploymentException, IOException, URISyntaxException {

        AgentEndpoint agentEndpoint = new AgentEndpoint();
        boolean isRegisteredAgent = false;
        ConsoleHelper.writeMessage("Please register as  /register agent name ");
        String userMessage;
        while (!isRegisteredAgent) {
            userMessage = ConsoleHelper.readString();
            String[] parts = spitMessage(userMessage);
            String register = parts[0];
            if (register.equalsIgnoreCase("/register") && parts.length == 3) {
                String userRole = parts[1];
                String userName = parts[2];
                isRegisteredAgent = true;
                agentEndpoint.sendRegisterMessage(createJsonRegisterAgentMessage(userName, userRole, register,"1"));
            } else {
                System.out.println("you entered incorrect data, please try again");
            }
        }

        while (true) {
            userMessage = ConsoleHelper.readString();
            if (userMessage.equals("/exit")) {
                 agentEndpoint.sendMessage(createJsonMessage("exitAgent","/exit"));
                break;
            }
            if (userMessage.equals("/leave")) {
                //  chatRoomClientEndpoint.sendMessage(createJsonMessage(userMessage,"/leave"));
            }

            if (!userMessage.equals("/leave"))
                agentEndpoint.sendMessage(createJsonMessage(userMessage, "new chatting"));
        }

    }

    public static String createJsonRegisterAgentMessage(String userName, String userRole, String typeOfMessage, String maxCountActiveChat) {
        JsonObject jsonObject = Json.createObjectBuilder().add("message", userRole + " " + userName).add("TypeOfMessage", typeOfMessage).add("maxCountActiveChat", maxCountActiveChat).add("TypeofAgent", "console").build();
        StringWriter stringWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
            jsonWriter.write(jsonObject);
        }
        return stringWriter.toString();
    }




    private static String createJsonMessage(String message, String typeOfMessage) {

        JsonObject jsonObject = Json.createObjectBuilder().add("message", message).add("TypeOfMessage", typeOfMessage).build();
        StringWriter stringWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
            jsonWriter.write(jsonObject);
        }
        return stringWriter.toString();
    }


    private static String[] spitMessage(String userMessage) {
        return userMessage.split(" ");
    }


}
