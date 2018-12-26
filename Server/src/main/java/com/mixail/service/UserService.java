package com.mixail.service;


import com.mixail.message.MessageUtil;
import com.mixail.model.Status;
import com.mixail.model.TypeOfUser;
import com.mixail.model.User;
import com.mixail.repository.UserRepositoryImpl;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);
    static Properties property = new Properties();
    static UserRepositoryImpl repository = new UserRepositoryImpl();
    private MessageUtil messageUtil = new MessageUtil();
    private final Object writeLock = new Object();
    public static final ConcurrentLinkedQueue<User> clientsQueue = new ConcurrentLinkedQueue<>();
    private static volatile UserService instance;


    public static UserService getInstance() {
        UserService localInstance = instance;
        if (localInstance == null) {
            synchronized (UserService.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new UserService();
                    try {
                        ClassLoader classLoader = UserService.class.getClassLoader();
                        property.load(classLoader.getResourceAsStream("server.messages.properties"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return localInstance;
    }


    public List<User> getWaitingClients() {
        return new LinkedList<>(clientsQueue);
    }

    public static Map<String, String> splitMessages(String message) {
        Map<String, String> userParameters = new HashMap<>();
        String[] splitMessage = message.split(" ");
        String userType = splitMessage[0];
        String name = splitMessage[1];
        userParameters.put("name", name);
        userParameters.put("userType", userType);
        return userParameters;

    }


    public void registrationUser(String message, User user) {
        Map<String, String> userParameters = splitMessages(message);
        String userType = userParameters.get("userType");
        String name = userParameters.get("name");

        if (userType.equals("agent")) {
            registrationAgent(user, name);
        }
        if (userType.equals("client")) {
            registrationClient(user, name);
        }
    }

    public void registrationClient(User user, String name) {
        user.setName(name);
        user.setTypeOfUser(TypeOfUser.CLIENT);
        repository.addClient(user);
        logger.info("INFO: Registered new client " + user.getName());

        synchronized (UserService.class) {
            clientsQueue.add(user);
        }
        try {
            synchronized (writeLock) {
                user.getUserSession().getBasicRemote().sendText(MessageUtil.buildJsonData("System", "Client " + name + " is registered"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void registrationAgent(User user, String name) {
        user.setName(name);
        user.setTypeOfUser(TypeOfUser.AGENT);
        repository.addAgent(user);
        logger.info("INFO: Registered new agent " + user.getName());
        messageUtil.sendAgentRegisterMessage(user);
        newChattingAgent("find client", user);
    }


    public boolean hasReadyAgent() {
        return getAgents().stream().anyMatch(user -> user.ready());
    }


    public Collection<User> getAgents() {
        return repository.getAgents();
    }


    public Collection<User> getClients() {
        return repository.getClients();
    }


    public void newChattingClient(String message, User client) {


        if (client.getStatus() == Status.Talking) {
            messageUtil.sendClientMessageToAgent(client, "Chatting", message);
            messageUtil.sendMessageToClient(client, message, client.getName());
        }


        if (client.getStatus() == Status.Registered || client.getStatus() == Status.Waiting) {
            User agent;
            if (hasReadyAgent()) {
                agent = getAgents().stream().filter(person -> person.ready()).findAny().get();
                messageUtil.sendSystemMessageToClient(client, "Agent " + agent.getName() + " connected to you");
                clientsQueue.remove(client);
                messageUtil.sendAgentFindClientMessage(agent, client);
                agent.addClientToRoom(client.getId(), client);
                client.setUserForChat(agent);
                client.setStatus(Status.Talking);
                agent.setStatus(Status.Talking);
                for (String firstMes : client.getFirstMessages()) {
                    if (firstMes != message)
                        messageUtil.sendClientMessageToAgent(client, "Chatting", firstMes);
                }
                messageUtil.sendMessageToClient(client, message, client.getName());
                messageUtil.sendClientMessageToAgent(client, "Chatting", message);
                client.getFirstMessages().clear();
                logger.info("dialog between agent " + agent.getName() + " client " + client.getName() + " was started");
            } else {
                messageUtil.sendSystemMessageToClient(client, property.getProperty("message.noFreeAgents"));
                messageUtil.sendMessageToClient(client, message, client.getName());
                client.getFirstMessages().add(message);
            }
        }

    }


    public void newChattingAgent(String message, User agent) {

        if (agent.getStatus() == Status.Talking) {
            if (!agent.isConsoleAgent()) {
                messageUtil.sendAgentMessageToClient(agent, message);
                messageUtil.sendAgentMessageToHimself(agent, message);
            } else {
                messageUtil.sendAgentMessageToConsoleClient(agent, message);
                messageUtil.sendAgentMessageToConsoleHimself(agent, message);
            }
        }

        if (agent.getStatus() == Status.Registered) {

            for (int i = 0; i < agent.getMaxCountActiveChat(); i++) {
                User client;
                synchronized (this) {
                    client = clientsQueue.poll();
                }
                if (client != null) {
                    try {
                        agent.getUserSession().getBasicRemote().sendText(messageUtil.createJsonMessageToAgent(agent, client, "find", "find" + client.getId()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    agent.addClientToRoom(client.getId(), client);
                    client.setUserForChat(agent);
                    client.setStatus(Status.Talking);
                    agent.setStatus(Status.Talking);
                    messageUtil.sendSystemMessageToClient(client, "Agent " + agent.getName() + " connected to you");
                    for (String firstMes : client.getFirstMessages()) {
                        messageUtil.sendClientMessageToAgent(client, "Chatting", firstMes);
                    }
                    client.getFirstMessages().clear();
                    logger.info("dialog between agent " + agent.getName() + " client " + client.getName() + " was started");
                }
            }
        }
    }

    public void clientExit(User client) {
        if (client.getUserForChat() != null) {
            messageUtil.sendClientMessageToAgent(client, "ExitClient", "Client exited from the chat,the tab was close");
            client.getUserForChat().removeClientFromRoom(client.getId());
        }
        logger.info("client " + client.getName() + " exit from the chat");
        repository.removeClient(client);
    }

    public void leaveClient(User client) {
        messageUtil.sendSystemMessageToClient(client, property.getProperty("message.client.left"));
        if (client.getStatus() != Status.Waiting) {
            messageUtil.sendClientMessageToAgent(client, "ClientLeft", "dialog is ended, client left chat");
            client.getUserForChat().removeClientFromRoom(client.getId());
            client.setStatus(Status.Waiting);
        }
        logger.info("client " + client.getName() + " left the chat");

    }

    public void closeTabAgent(String message, User agent) {
        Map<String, String> stringStringMap = messageUtil.parseAgentMessage(message);
        User client = agent.getClientFromRoom(Integer.parseInt(stringStringMap.get("id")));
        messageUtil.sendSystemMessageToClient(client, property.getProperty("message.agent.exit"));
        agent.removeClientFromRoom(Integer.parseInt(stringStringMap.get("id")));
        client.setUserForChat(null);
        client.setStatus(Status.Waiting);
        logger.info("agent " + agent.getName() + " end dialog with " + client.getName());
    }

    public void exitAgent(User agent) {
        for (User client : agent.getInterlocutors().values()) {
            messageUtil.sendSystemMessageToClient(client, property.getProperty("message.agent.exit"));
            client.setStatus(Status.Waiting);
            client.setUserForChat(null);
            clientsQueue.add(client);
        }
        agent.getInterlocutors().clear();
        repository.removeAgent(agent);
    }

    public User getClientById(int id) {
        return getClients().stream().filter(user -> id == user.getId()).findFirst().get();
    }


    public User getAgentById(int id) {
        return getAgents().stream().filter(user -> id == user.getId()).findFirst().get();

    }


    public String creatGsonList(List<User> listOfUsers) {
        JsonObject jsonObject;
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

        for (User user : listOfUsers) {
            objectBuilder.add(user.getId() + " ", user.getName());
        }
        jsonObject = objectBuilder.build();

        StringWriter Writer = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(Writer)) {
            jsonWriter.write(jsonObject);
        }
        return Writer.toString();
    }

    public List<User> getReadyAgents() {
        return getAgents().stream()
                .filter(agent -> agent.ready())
                .collect(Collectors.toList());
    }

}
