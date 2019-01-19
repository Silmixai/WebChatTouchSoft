package com.mixail.service;

import com.mixail.model.User;
import com.mixail.repository.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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

        User client1 = new User();
        client1.setName("client1");
        User client2 = new User();
        client2.setName("client2");

        User agent1 = new User();
        client1.setName("agent1");
        User agent2 = new User();
        client2.setName("agent2");


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


    }

    @Test
    void getAgents() {


    }

    @Test
    void getClients() {

    //    User client1 = new User();
   //     User client2 = new User();
     //   userService.registrationClient(client1,client1.getName());
    //    userService.registrationClient(client2,client2.getName());

    }

    @Test
    void exitAgent() {


    }

    @Test
    void getClientById() {


    }
}