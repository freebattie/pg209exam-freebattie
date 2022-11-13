package no.kristiania.socialbuzz.endpoints;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.socialbuzz.dao.Chat;
import no.kristiania.socialbuzz.dao.DaoChat;

import java.sql.SQLException;
import java.util.List;

@Path("/chats")
public class ChatsEndpoint {

    @Inject
    private DaoChat daoChat;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Chat> getAllChats(@PathParam("id")long id) throws SQLException {
        return daoChat.getAllChats(id);
    }

    // TODO: 12.11.2022 Implement this post correctly
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    public void makeNewChat(List<User> users, String title) throws SQLException {
//        daoChat.makeNewChat(users, title);
//    }

}
