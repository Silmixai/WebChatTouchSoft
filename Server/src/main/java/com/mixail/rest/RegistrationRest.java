package com.mixail.rest;


import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.DeploymentException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;

import static com.mixail.Agent.createJsonRegisterAgentMessage;
import static com.mixail.Client.createJsonRegisterMessage;

@Path("/reg")
public class RegistrationRest {
    @Path("/client")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    public String registerClient(
            @FormParam("name") String name,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response
    )

    {
        try {
            RestClientEndpoint restClientEndpoint= new RestClientEndpoint();
            restClientEndpoint.sendMessage(createJsonRegisterMessage(name, "client", "/register"));
            HttpSession session = request.getSession(true);
            session.setMaxInactiveInterval(120);
            session.setAttribute("restClientEndpoint", restClientEndpoint);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DeploymentException e) {
            e.printStackTrace();
        }
        JsonObject jsonObject = Json.createObjectBuilder().add("register rest client name ",name).build();
        StringWriter stringWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
            jsonWriter.write(jsonObject);
        }
        return  stringWriter.toString();
    }




    @Path("/agent")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    public String registerAgent(
            @FormParam("name") String name,
            @FormParam("count") String activeChats,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response
    ) {
        if (name != null && Integer.parseInt(activeChats) > 0) {
            try {
                HttpSession session = request.getSession(true);
                session.setMaxInactiveInterval(120);
                RestAgentEndpoint restAgentEndpoint= new RestAgentEndpoint();
                session.setAttribute("restAgentEndpoint", restAgentEndpoint);
                restAgentEndpoint.sendMessage(createJsonRegisterAgentMessage(name, "agent", "/register",activeChats));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DeploymentException e) {
                e.printStackTrace();
            }

        } else {
            try {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JsonObject jsonObject = Json.createObjectBuilder().add("register rest agent name ",name).build();
        StringWriter stringWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
            jsonWriter.write(jsonObject);
        }
        return  stringWriter.toString();
    }

}
