package com.mixail.service;

import com.mixail.model.User;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

public class ChatService {

    UserService userService= UserService.getInstance();

    public String getOpenChats() {

        List<User> collect = userService.getAgents().stream().filter(user -> user.getInterlocutorsCount() != 0).collect(Collectors.toList());
        JsonObject jsonObject;
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

        for (User agent :collect) {
            objectBuilder.add(agent+" working with ",agent.getInterlocutors().values().toString());
        }
        jsonObject = objectBuilder.build();
        StringWriter stringWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
            jsonWriter.write(jsonObject);
        }
        return  stringWriter.toString();
    }
}
