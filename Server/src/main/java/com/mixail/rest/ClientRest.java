package com.mixail.rest;


import com.mixail.service.ClientService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/clients")
public class ClientRest {

    ClientService clientService= new ClientService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getWaitingClients() {
        return clientService.getWaitingClients();
    }

    //Получить детальную информацию об одном указанном клиенте
    @GET
    @Path("/{id}")
    public String getClientById(@PathParam("id") Integer id) {
        return clientService.getClientById(id);
    }


}
