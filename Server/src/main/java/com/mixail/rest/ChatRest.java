package com.mixail.rest;

import com.mixail.service.ChatService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/chats")
public class ChatRest {

    //Получить текущие открытые чаты

    ChatService chatService= new ChatService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getRooms(
    ) {
        return chatService.getOpenChats();
    }

}
