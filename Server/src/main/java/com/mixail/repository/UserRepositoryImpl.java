package com.mixail.repository;

import com.mixail.model.User;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepositoryImpl implements UserRepository {

    private static ConcurrentHashMap<Integer, User> clients = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Integer, User> agents = new ConcurrentHashMap<>();

    static {
        User user = new User();
        user.setName("mike");
        user.setPassword("1111");
        user.setMaxCountActiveChat(2);
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

}
