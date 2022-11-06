package no.kristiania.socialbuzz.endpoints;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.socialbuzz.Chat;
import no.kristiania.socialbuzz.User;
import no.kristiania.socialbuzz.db.DaoUser;

import java.util.List;

@Path("/chat-list")
public class ChatListEndpoint {

    @Inject
    private DaoUser daoUser;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Chat> getAllChat(User user) {

        return null;
    }

}
