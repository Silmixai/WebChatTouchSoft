package com.mixail.rest;


import com.mixail.service.AgentService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.*;


@Path("/agents")
public class AgentRest {


    AgentService agentService= new AgentService();

    @GET
    @Path("/allRegister")
    @Tag(name = "Get all registered agents")
    public String getAgentById() {
        return agentService.getAllRegister();
    }


    // Получить детальную информацию об одном указанном агенте
    @GET
    @Tag(name = "Get detailed information about one specified agent")
    @Path("/{id}")
    public String getAgentById(@PathParam("id") Integer id) {
        return agentService.getAgentById(id);
    }


    // Получить количество свободных агентов
    @GET
    @Tag(name = "Get the number of free agents")
    @Path("/ready/count")
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json"
            )

    )

    public String getCountReadyAgents() {
        return agentService.getSizeReadyAgents();
    }


    //Получить всех свободных агентов

    @GET
    @Tag(name = "Get all  free agents")
    @Path("/ready")
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = String.class)
                    )
            )

    )
    public String getReadyAgents(@DefaultValue("0") @QueryParam("page") int page, @DefaultValue("0") @QueryParam("count") int count)
    {

        return agentService.getReadyAgents(page, count);
    }

}
