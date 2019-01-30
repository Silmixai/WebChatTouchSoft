package com.mixail.service;

import com.mixail.model.User;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import java.io.StringWriter;
import java.util.List;

public class ClientService {

    UserService userService = UserService.getInstance();


    public String getWaitingClients() {
        List<User> waitingClients = userService.getWaitingClients();

        if (waitingClients.size()==0)
        {
            JsonObject jsonObject = Json.createObjectBuilder().add("info:", "there is no waiting client ").build();
            return jsonObject.toString() ;
        }

        return userService.creatGsonList(waitingClients);
    }


    public String getClientById(int id) {
        User clientById = userService.getClientById(id);

        if (clientById==null)
        {
            JsonObject jsonObject = Json.createObjectBuilder().add("info:", "there is no client with such id").build();
            return jsonObject.toString() ;

        }
        JsonObject jsonObject = Json.createObjectBuilder().add("id", clientById.getId()).add("name", clientById.getName()).add("status", clientById.getStatus().toString()).add("userSession", clientById.getUserSession().toString()).build();
        StringWriter stringWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
            jsonWriter.write(jsonObject);
        }
        return stringWriter.toString();
    }

    public void exitClient(Integer id) {

        User clientById = userService.getClientById(id);
        userService.clientExit(clientById);
    }
}
