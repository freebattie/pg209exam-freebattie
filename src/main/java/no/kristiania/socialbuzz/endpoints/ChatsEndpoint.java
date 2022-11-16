package no.kristiania.socialbuzz.endpoints;

import com.google.gson.Gson;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.socialbuzz.dto.Chat;
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void makeNewChat(String chatJson) throws SQLException {
        Gson gson = new Gson();
        var chat = gson.fromJson(chatJson, Chat.class);
        daoChat.makeNewChat(chat.getUserIdList(), chat.getTitle());
    }

}
