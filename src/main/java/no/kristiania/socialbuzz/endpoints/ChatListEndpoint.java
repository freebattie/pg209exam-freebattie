package no.kristiania.socialbuzz.endpoints;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.socialbuzz.dao.Chat;
import no.kristiania.socialbuzz.dao.DaoChat;

import java.sql.SQLException;
import java.util.List;

@Path("/chats")
public class ChatListEndpoint {

    @Inject
    private DaoChat daoChat;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Chat> getAllChats(@PathParam("id")long id) throws SQLException {
        return daoChat.getAllChats(id);
    }

}
