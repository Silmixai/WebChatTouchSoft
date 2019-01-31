package com.mixail.service;

import com.mixail.model.TypeOfUser;
import com.mixail.model.User;
import com.mixail.repository.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


class UserServiceTest {


    private static UserRepositoryImpl repository;
    private static UserService userService;
    private static Session userSession;
    private static RemoteEndpoint.Basic remoteEndpoint;

    @BeforeAll
    public static void setUp() {

        repository = mock(UserRepositoryImpl.class);
        userSession = mock(Session.class);
        remoteEndpoint = mock(RemoteEndpoint.Basic.class);
        userService = UserService.getInstance();
        userService.setRepository(repository);
        try {
            doNothing().when(remoteEndpoint).sendText(String.valueOf(String.class));
        } catch (IOException e) {
            e.printStackTrace();
        }


        userSession = new Session() {
            @Override
            public WebSocketContainer getContainer() {
                return null;
            }

            @Override
            public void addMessageHandler(MessageHandler handler) throws IllegalStateException {

            }

            @Override
            public <T> void addMessageHandler(Class<T> clazz, MessageHandler.Whole<T> handler) {

            }

            @Override
            public <T> void addMessageHandler(Class<T> clazz, MessageHandler.Partial<T> handler) {

            }

            @Override
            public Set<MessageHandler> getMessageHandlers() {
                return null;
            }

            @Override
            public void removeMessageHandler(MessageHandler handler) {

            }

            @Override
            public String getProtocolVersion() {
                return null;
            }

            @Override
            public String getNegotiatedSubprotocol() {
                return null;
            }

            @Override
            public List<Extension> getNegotiatedExtensions() {
                return null;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public boolean isOpen() {
                return false;
            }

            @Override
            public long getMaxIdleTimeout() {
                return 0;
            }

            @Override
            public void setMaxIdleTimeout(long milliseconds) {

            }

            @Override
            public void setMaxBinaryMessageBufferSize(int length) {

            }

            @Override
            public int getMaxBinaryMessageBufferSize() {
                return 0;
            }

            @Override
            public void setMaxTextMessageBufferSize(int length) {

            }

            @Override
            public int getMaxTextMessageBufferSize() {
                return 0;
            }

            @Override
            public RemoteEndpoint.Async getAsyncRemote() {
                return null;
            }

            @Override
            public RemoteEndpoint.Basic getBasicRemote() {
                return remoteEndpoint;
            }

            @Override
            public String getId() {
                return null;
            }

            @Override
            public void close() throws IOException {

            }

            @Override
            public void close(CloseReason closeReason) throws IOException {

            }

            @Override
            public URI getRequestURI() {
                return null;
            }

            @Override
            public Map<String, List<String>> getRequestParameterMap() {
                return null;
            }

            @Override
            public String getQueryString() {
                return null;
            }

            @Override
            public Map<String, String> getPathParameters() {
                return null;
            }

            @Override
            public Map<String, Object> getUserProperties() {
                return null;
            }

            @Override
            public Principal getUserPrincipal() {
                return null;
            }

            @Override
            public Set<Session> getOpenSessions() {
                return null;
            }
        };


    }


    @Test
    void closeTabAgent()
    {

        User client6 = new User();
        client6.setUserSession(userSession);
        client6 .setName("client");

        User agent6 = new User();
        agent6.setUserSession(userSession);
        agent6.setName("agent");

        agent6.addClientToRoom(client6.getId(),client6);
        assertEquals(agent6.getInterlocutorsCount().intValue(),1);
        JsonObject message = Json.createObjectBuilder().add("message", "message").add("id",client6.getId().toString()).add("clientName",client6.getName()).build();
        userService.closeTabAgent(message.toString(),agent6);
        assertEquals(agent6.getInterlocutorsCount().intValue(),0);
    }


    @Test
    void splitMessages() {
        String message = "agent Mike";
        Map<String, String> userParameters = UserService.splitMessages(message);
        String userType = userParameters.get("userType");
        String name = userParameters.get("name");
        assertEquals(userType, "agent");
        assertEquals(name, "Mike");
    }


    @Test
    void addAgent() {
        User agent = new User();
        agent.setUserSession(userSession);
        agent.setName("agent");
        userService.registrationAgent(agent, agent.getName());
        verify(repository).addAgent(agent);
    }


    @Test
    void addClient() {
        User client = new User();
        client.setUserSession(userSession);
        client.setName("client");
        userService.registrationClient(client, client.getName());
        verify(repository).addClient(client);
    }


    @Test
    void getWaitingClients() {


        User client = new User();
        client.setUserSession(userSession);
        client.setName("client");
        userService.registrationClient(client, " ");

        List<User> waitingClients = userService.getWaitingClients();
        assertEquals(waitingClients.size(),6 );

    }

    @Test
    void getAgents() {

        User agent1 = new User();
        agent1.setUserSession(userSession);
        agent1.setName("client");

        User agent2 = new User();
        agent2.setUserSession(userSession);
        agent2.setName("client");

        userService.registrationClient(agent1, " ");
        userService.registrationClient(agent2, " ");

        userService.getAgents();
        verify(repository).getAgents();

    }

    @Test
    void getClients() {

        User client1 = new User();
        client1.setUserSession(userSession);
        client1.setName("client");

        User client2 = new User();
        client2.setUserSession(userSession);
        client2.setName("client");

        userService.registrationClient(client1, " ");
        userService.registrationClient(client2, " ");

        userService.getClients();
        verify(repository).getClients();


    }

    @Test
    void exitAgent() {

        User agent1 = new User();
        agent1.setUserSession(userSession);
        agent1.setName("client");
        userService.registrationAgent(agent1,agent1.getName());
        userService.exitAgent(agent1);
        verify(repository).removeAgent(agent1);

    }

    @Test
    void clientExit()
    {
        User client5 = new User();
        client5 .setUserSession(userSession);
        client5 .setName("client");
        userService.registrationAgent(client5 ,client5.getName());
        userService.exitAgent(client5);
        verify(repository).removeAgent(client5);

    }


    @Test
    void registrationUser()
    {
        User client7 = new User();
        client7 .setUserSession(userSession);
        client7 .setName("client");
        client7.setTypeOfUser(TypeOfUser.CLIENT);

        userService.registrationUser("client "+client7.getName(),client7);

        verify(repository).addClient(client7);


        User agent8 = new User();
        agent8 .setUserSession(userSession);
        agent8 .setName("agent8");
        agent8.setTypeOfUser(TypeOfUser.AGENT);

        userService.registrationUser("agent "+agent8.getName(),agent8);

        verify(repository).addAgent(agent8);

    }



    @Test
    void registrationAgent()
    {
        User client9 = new User();
        client9 .setUserSession(userSession);
        client9 .setName("client");
        client9.setTypeOfUser(TypeOfUser.CLIENT);

        userService.registrationUser("client "+client9.getName(),client9);
        verify(repository).addClient(client9);
    }


    @Test
    void registrationClient()
    {
        User agent9 = new User();
        agent9 .setUserSession(userSession);
        agent9 .setName("agent9");
        agent9.setTypeOfUser(TypeOfUser.AGENT);
        userService.registrationAgent(agent9,agent9.getName());
        verify(repository).addAgent(agent9);
    }

}