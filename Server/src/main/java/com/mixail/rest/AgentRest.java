package com.mixail.rest;


import com.mixail.service.AgentService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/agents")
public class AgentRest {

    AgentService agentService= new AgentService();


    // Получить детальную информацию об одном указанном агенте
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAgentById(@PathParam("id") Integer id) {
        return agentService.getAgentById(id);
    }


    // Получить количество свободных агентов
    @GET
    @Path("/ready/count")
    public String getCountReadyAgents() {
        return agentService.getSizeReadyAgents();
    }


    //Получить всех свободных агентов
    @GET
    @Path("/ready")
    @Produces(MediaType.APPLICATION_JSON)
    public String getReadyAgents(@DefaultValue("0") @QueryParam("page") int page, @DefaultValue("0") @QueryParam("count") int count)
    {
        return agentService.getReadyAgents(page, count);
    }


}
