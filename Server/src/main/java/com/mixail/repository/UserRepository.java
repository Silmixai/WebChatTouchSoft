package com.mixail.repository;

import com.mixail.model.User;

import java.util.Collection;

public interface UserRepository {

     void addClient(User person);
     void removeClient(User person);
     void addAgent(User person);
     void removeAgent(User person);
     Collection<User> getAgents();
     Collection<User> getClients();

}
