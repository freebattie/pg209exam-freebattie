package no.kristiania.socialbuzz.endpoints;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.socialbuzz.dao.Chat;
import no.kristiania.socialbuzz.dao.DaoChat;

import java.sql.SQLException;
import java.util.List;

@Path("/chat-list")
public class ChatListEndpoint {

    @Inject
    private DaoChat daoChat;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Chat> getAllChats(int idUser) throws SQLException {
        return daoChat.getAllChats(idUser);
    }

}
