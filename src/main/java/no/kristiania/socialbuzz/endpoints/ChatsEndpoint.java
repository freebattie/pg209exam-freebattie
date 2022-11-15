package no.kristiania.socialbuzz.endpoints;

import com.google.gson.Gson;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.socialbuzz.dto.Chat;
import no.kristiania.socialbuzz.dao.DaoChat;
import no.kristiania.socialbuzz.dto.User;

import java.sql.SQLException;
import java.util.List;

@Path("/chats")
public class ChatsEndpoint {

    @Inject
    private DaoChat daoChat;

    @GET
    @Path("/{id}/{notid}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Chat> getAllChats(@PathParam("id")long id ,@PathParam("notid")long notid) throws SQLException {
        return daoChat.getAllChats(id);
    }

    // TODO: 12.11.2022 Implement this post correctly
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void makeNewChat(String chat) throws SQLException {
        Gson gson = new Gson();
        var newChat = gson.fromJson(chat,Chat.class);
        System.out.println(newChat.getTitle() +" "+ newChat.getUsersListId());
        daoChat.makeNewChat(newChat.getUsersListId(),newChat.getTitle());

    }

}
