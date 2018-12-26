package com.mixail.message;

import com.mixail.model.User;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class MessageUtil {


    private final Object writeLock = new Object();

    public static String buildJsonData(String username, String message) {

        JsonObject jsonObject = Json.createObjectBuilder().add("message", username + " : " + message).build();
        StringWriter stringWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
            jsonWriter.write(jsonObject);
        }
        return stringWriter.toString();
    }


    public Map<String, String> parseAgentMessage(String message) {
        Map<String, String> parseMessage = new HashMap<>();
        String mes = Json.createReader(new StringReader(message)).readObject().getString("message");
        String id = Json.createReader(new StringReader(message)).readObject().getString("id");
        String clientName = Json.createReader(new StringReader(message)).readObject().getString("clientName");
        parseMessage.put("mes", mes);
        parseMessage.put("id", id);
        parseMessage.put("clientName", clientName);
        return parseMessage;
    }


    public void sendClientMessageToAgent(User client, String TypeOfMessage, String message)
    {
        try {
            synchronized (writeLock) {
                client.getUserForChat().getUserSession().getBasicRemote().sendText(createJsonMessageToAgent(client.getUserForChat(), client, TypeOfMessage, message));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void sendAgentMessageToClient(User agent, String message) {
        Map<String, String> parseMessage = parseAgentMessage(message);
        try {
            {
                agent.getClientFromRoom(Integer.parseInt(parseMessage.get("id"))).getUserSession().getBasicRemote().sendText(MessageUtil.buildJsonData(agent.getName(), parseMessage.get("mes")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String createJsonMessageToAgent(User agent, User client, String TypeOfMessage, String message) {

        JsonObject jsonObject = Json.createObjectBuilder().add("id", client.getId()).add("clientName", client.getName()).add("TypeOfMessage", TypeOfMessage).add("message", message).add("agentName", agent.getName()).build();
        StringWriter stringWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
            jsonWriter.write(jsonObject);
        }
        return stringWriter.toString();
    }


    public void sendAgentMessageToHimself(User agent, String message) {

        Map<String, String> parseMessage = parseAgentMessage(message);
        try {
            synchronized (writeLock) {
                agent.getUserSession().getBasicRemote().sendText(createJsonMessageToAgent(agent, agent.getClientFromRoom(Integer.parseInt(parseMessage.get("id"))), "Chatting Agent", parseMessage.get("mes")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendSystemMessageToClient(User user, String message) {
        try {
            synchronized (writeLock) {
                user.getUserSession().getBasicRemote().sendText(MessageUtil.buildJsonData("System", message));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToClient(User user, String message, String name) {
        try {
            synchronized (writeLock) {
                user.getUserSession().getBasicRemote().sendText(MessageUtil.buildJsonData(name, message));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendAgentRegisterMessage(User agent) {

        JsonObject jsonObject = Json.createObjectBuilder().add("agentName", agent.getName()).add("agentId",agent.getId()).add("TypeOfMessage","Register").build();
        StringWriter stringWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
            jsonWriter.write(jsonObject);
        }
        try {
            synchronized (writeLock) {
                agent.getUserSession().getBasicRemote().sendText(stringWriter.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendAgentFindClientMessage(User agent,User client) {
        try {
            synchronized (writeLock) {
                agent.getUserSession().getBasicRemote().sendText(createJsonMessageToAgent(agent, client, "find", "Я клиент id" + client.getId()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendAgentMessageToConsoleClient(User agent, String message) {
        User client= agent.getInterlocutors().values().stream().findFirst().get();
        try {
            String mes = Json.createReader(new StringReader(message)).readObject().getString("message");
            client.getUserSession().getBasicRemote().sendText(buildJsonData(agent.getName(),mes));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendAgentMessageToConsoleHimself(User agent, String message) {

        String mes = Json.createReader(new StringReader(message)).readObject().getString("message");
        JsonObject jsonObject = Json.createObjectBuilder().add("message", mes).add("TypeOfMessage", "Chatting Agent").add("agentName",agent.getName()).build();
        StringWriter stringWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
            jsonWriter.write(jsonObject);
        }
        try {
            agent.getUserSession().getBasicRemote().sendText(jsonObject.toString());
            System.out.println("sendAgentMessageToConsoleHimself"+jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
