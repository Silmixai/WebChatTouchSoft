package com.mixail;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.websocket.DeploymentException;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;

public class Client {

    public static void main(String[] args) throws DeploymentException, IOException, URISyntaxException {

        ChatRoomClientEndpoint chatRoomClientEndpoint = new ChatRoomClientEndpoint();
        boolean isRegistered = false;
        ConsoleHelper.writeMessage("Please register as  /register client name ");
        String userMessage;
        while (!isRegistered) {
            userMessage = ConsoleHelper.readString();
            String[] parts = spitMessage(userMessage);
            String register = parts[0];
            if (register.equalsIgnoreCase("/register") && parts.length == 3) {
                String userRole = parts[1];
                String userName = parts[2];
                isRegistered = true;
                chatRoomClientEndpoint.sendMessage(createJsonRegisterMessage(userName, userRole, register));
            } else {
                System.out.println("you entered incorrect data, please try again");
            }
        }


        while (true) {
            userMessage = ConsoleHelper.readString();
            if (userMessage.equals("/exit")) {
                chatRoomClientEndpoint.sendMessage(createJsonMessage(userMessage,"/exit"));
                break;
            }
            if (userMessage.equals("/leave")) {
                chatRoomClientEndpoint.sendMessage(createJsonMessage(userMessage,"/leave"));
            }

            if (!userMessage.equals("/leave"))
                chatRoomClientEndpoint.sendMessage(createJsonMessage(userMessage,"new chatting"));
        }
    }

    public static String createJsonRegisterMessage(String userName, String userRole, String typeOfMessage) {

        JsonObject jsonObject = Json.createObjectBuilder().add("message", userRole + " " + userName).add("TypeOfMessage", typeOfMessage).build();
        StringWriter stringWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
            jsonWriter.write(jsonObject);
        }
        return stringWriter.toString();
    }

    public static String createJsonMessage(String message, String typeOfMessage) {
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
