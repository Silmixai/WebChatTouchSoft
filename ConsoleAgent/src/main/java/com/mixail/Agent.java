package com.mixail;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.websocket.DeploymentException;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.Base64;

public class Agent {

    static boolean isRegisteredAgent = false;
    static boolean isCorrectSignIn = false;


    public static void main(String[] args) throws DeploymentException, IOException, URISyntaxException {

        AgentEndpoint agentEndpoint = new AgentEndpoint();

        ConsoleHelper.writeMessage("input '/sign in agent your_name' to login as agent ");
        String userMessage;


        while (true) {

            userMessage = ConsoleHelper.readString();

            while (!isRegisteredAgent) {

                String[] parts = spitMessage(userMessage);
                String register = parts[0];
                if (register.equalsIgnoreCase("/sign") && parts.length == 4) {
                    String userName = parts[3];
                    //agentEndpoint.sendRegisterMessage(createJsonRegisterAgentMessage(userName, userRole, register,"1"));
                    ConsoleHelper.writeMessage("Input password");
                    String agentPassword = ConsoleHelper.readString();
                    Base64.Encoder encoder = Base64.getEncoder();
                    String encodedString = encoder.encodeToString(agentPassword.getBytes());
                    agentEndpoint.sendRegisterMessage(createJsonSignInAgentMessage(userName, encodedString));
                    isRegisteredAgent = true;
                } else {
                    System.out.println("you entered incorrect data, please try again");
                    break;
                }
            }


            if (userMessage.equals("/exit")) {
                agentEndpoint.sendMessage(createJsonMessage("exitAgent", "/exit"));
                break;
            }
            if (userMessage.equals("/leave")) {
                //  chatRoomClientEndpoint.sendMessage(createJsonMessage(userMessage,"/leave"));
            }

            if (!userMessage.equals("/leave"))
                agentEndpoint.sendMessage(createJsonMessage(userMessage, "new chatting"));
        }

    }

    public static String createJsonSignInAgentMessage(String userName, String agentPassword) {
        JsonObject jsonObject = Json.createObjectBuilder().add("agentPassword", agentPassword).add("TypeOfMessage", "/signIn").add("TypeofAgent", "console").add("name", userName).build();
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
