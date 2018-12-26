package com.mixail.service;

import com.mixail.model.TypeOfUser;
import com.mixail.model.User;
import com.mixail.repository.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.websocket.Session;
import java.util.Map;

import static org.junit.Assert.assertEquals;


class UserServiceTest {

    static UserRepositoryImpl repository;

    private static User[] clientUsers;
    private static User[] agentUsers;

    @Mock
    private UserService userService;
    @Mock
    private static Session userSession;


    @BeforeAll
    public static void setUp() {
        repository = new UserRepositoryImpl();
        clientUsers = new User[5];
        agentUsers = new User[5];
        for (int i = 0; i < agentUsers.length; i++) {
            agentUsers[i] = new User();
            agentUsers[i].setName("client" + i);
            agentUsers[i].setTypeOfUser(TypeOfUser.AGENT);
        }
        for (int i = 0; i < clientUsers.length; i++) {
            clientUsers[i] = new User();
            clientUsers[i].setName("agent" + i);
            clientUsers[i].setTypeOfUser(TypeOfUser.CLIENT);
        }


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


    void getAgents() {
        //userService
        // when(userService.registrationAgent(user,"/register afent mike"))
    }

    @Test
    void getClients() {

        for (int i = 0; i < clientUsers.length; i++)
            repository.addClient(clientUsers[i]);

        assertEquals(5, repository.getClients().size());


        //        //   userService.registrationUser("client dima",user1);
        //assertEquals(userService.getClients()).size(), 2);
    }
}