package com.mixail.service;


import com.mixail.model.User;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

public class AgentService {

    UserService userService = UserService.getInstance();

    public String getSizeReadyAgents()
    {
        List<User> readyAgents = userService.getReadyAgents();

        JsonObject jsonObject = Json.createObjectBuilder().add("size of free agents", readyAgents.size()).build();
        StringWriter stringWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
            jsonWriter.write(jsonObject);
        }
        return stringWriter.toString();
    }


    //Получить детальную информацию об одном указанном агенте
    public String getAgentById(Integer id) {

        User agentById = userService.getAgentById(id);

        if (agentById==null)
        {
            JsonObject jsonObject = Json.createObjectBuilder().add("info:", "there is no agent with such id").build();
            return jsonObject.toString() ;

        }

        JsonObject jsonObject = Json.createObjectBuilder().add("id",agentById.getId()).add("name",agentById.getName()).add("maxCountActiveChat",agentById.getMaxCountActiveChat()).add("isConsoleAgent",agentById.isConsoleAgent()).build();
        StringWriter stringWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
            jsonWriter.write(jsonObject);
        }
        return  stringWriter.toString();
    }


    public String getReadyAgents(int parts, int count) {
        List<User> users = getUsers(userService.getReadyAgents(), parts, count);
        return  userService.creatGsonList(users);
    }

    private List<User> getUsers(List<User> users, int page, int count) {
        int skip = page != 0 ? (page - 1) * count : page;
        int end = count != 0 ? count : users.size();
        return users.stream()
                .skip(skip)
                .limit(end)
                .collect(Collectors.toList());
    }

    public String getAllRegister() {

        return userService.getAllRegister();
    }
}
