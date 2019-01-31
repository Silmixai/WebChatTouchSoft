package com.mixail.rest;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mixail.model.User;
import com.mixail.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Path("/msg")
public class MessagingRest {

    private Gson gson = new GsonBuilder().create();
    UserService userService = UserService.getInstance();

    @Path("/client/send")
    @POST
    @Tag(name = "Send a message to the agent from the client")
    @Consumes("application/x-www-form-urlencoded")
    public void SendRestClientMessage(
            @FormParam("message") String message,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response
    ) {
        try {
            RestClientEndpoint restClientEndpoint = (RestClientEndpoint) request.getSession().getAttribute("restClientEndpoint");
            restClientEndpoint.sendMessage(createJsonMessage(message, "new chatting"));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    @Path("/agent/send")
    @POST
    @Tag(name = "Send a message to the client from the agent")
    @Consumes("application/x-www-form-urlencoded")
    public void SendRestClientMessage(
            @FormParam("message") String message,
            @FormParam("id") String id,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response
    ) {

        try {
            RestAgentEndpoint restAgentEndpoint = (RestAgentEndpoint) request.getSession().getAttribute("restAgentEndpoint");
             restAgentEndpoint.sendMessage(createAgentJsonMessage(message,"new chatting",Integer.parseInt(id)));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Path("client/receive")
    @GET

    @Tag(name = "Get new client messages")
    @Produces(MediaType.APPLICATION_JSON)
    public String getClientMessage(
            @Context HttpServletRequest request,
            @Context HttpServletResponse response
    ) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            RestClientEndpoint restClientEndpoint = (RestClientEndpoint) request.getSession().getAttribute("restClientEndpoint");
            List<String> messages = new ArrayList<>(restClientEndpoint.getMessages());
            restClientEndpoint.clearMessageHistory();
            return gson.toJson(messages);
        } else {
            try {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Path("agent/receive")
    @GET
    @Tag(name = "Get new agent messages")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAgentMessage(
            @Context HttpServletRequest request,
            @Context HttpServletResponse response
    ) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            RestAgentEndpoint restAgentEndpoint = (RestAgentEndpoint) request.getSession().getAttribute("restAgentEndpoint");
            List<String> messages = new ArrayList<>(restAgentEndpoint.getMessages());
            restAgentEndpoint.clearMessageHistory();
            return gson.toJson(messages);
        } else {
            try {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String createJsonMessage(String message, String typeOfMessage) {

        JsonObject jsonObject = Json.createObjectBuilder().add("message", message).add("TypeOfMessage", typeOfMessage).build();
        StringWriter stringWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
            jsonWriter.write(jsonObject);
        }
        return stringWriter.toString();
    }


    private String createAgentJsonMessage(String message, String typeOfMessage, Integer id) {
        User clientById = userService.getClientById(id);
        String clientName = clientById.getName();

        JsonObject jsonObject = Json.createObjectBuilder().add("id",id.toString()).add("clientName",clientName).add("message",message).add("TypeOfMessage", typeOfMessage).build();
        StringWriter stringWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
            jsonWriter.write(jsonObject);
        }
        return stringWriter.toString();
    }





}
