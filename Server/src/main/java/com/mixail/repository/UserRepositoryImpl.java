package com.mixail.repository;

import com.mixail.model.AgentEntity;
import com.mixail.model.User;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserRepositoryImpl implements UserRepository {

    private static ConcurrentHashMap<Integer, User> clients = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Integer, User> agents = new ConcurrentHashMap<>();
    private static List<AgentEntity> registeredAgents = new CopyOnWriteArrayList<>();

    static {
        AgentEntity agentEntity = new AgentEntity();
        agentEntity.setName("agent1");
        agentEntity.setPassword("1111");
        agentEntity.setMaxCountActiveChat(2);
        registeredAgents.add(agentEntity);


            AgentEntity agentEntity2 = new AgentEntity();
            agentEntity2.setName("agent2");
            agentEntity2.setPassword("2222");
            agentEntity2.setMaxCountActiveChat(3);

            registeredAgents.add(agentEntity2);

            AgentEntity agentEntity3 = new AgentEntity();
            agentEntity3.setName("agent3");
            agentEntity3.setPassword("3333");
            agentEntity3.setMaxCountActiveChat(1);

            registeredAgents.add(agentEntity3);

    }


    @Override
    public void addClient(User user) {
        clients.put(user.getId(), user);
    }

    @Override
    public void removeClient(User user) {
        clients.remove(user.getId());
    }

    @Override
    public void addAgent(User user) {
        agents.put(user.getId(), user);
    }

    @Override
    public void removeAgent(User user) {
        agents.remove(user.getId());
    }

    @Override
    public Collection<User> getAgents() {
        return agents.values();
    }

    @Override
    public Collection<User> getClients() {
        return clients.values();
    }

    @Override
    public Collection<AgentEntity> getRegisterAgents() {
        return registeredAgents;
    }


}
