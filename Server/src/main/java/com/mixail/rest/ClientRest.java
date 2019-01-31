package com.mixail.rest;


import com.mixail.service.ClientService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/clients")
public class ClientRest {

    ClientService clientService= new ClientService();


    @GET
    @Tag(name = "Get all clients in the queue(waiting clients)")
    @Produces(MediaType.APPLICATION_JSON)
    public String getWaitingClients() {

        return clientService.getWaitingClients();
    }


    @Path("/{id}")
    @Tag(name = "Get detailed information about one specified client")
    @GET
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json"
            )
    )

    public String getClientById(@PathParam("id") Integer id) {
        return clientService.getClientById(id);
    }





    /*

    @Path("/exitClient")
    @POST
    @Tag(name = "exit client")
    @Consumes("application/x-www-form-urlencoded")
    public String exitClient(
            @FormParam("id")Integer id )

    {

        clientService.exitClient(id);

        return "client exit";

    }
    */


}
