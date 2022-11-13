package no.kristiania.socialbuzz.endpoints;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.socialbuzz.dao.DaoMessage;
import no.kristiania.socialbuzz.dao.Message;

import java.sql.SQLException;
import java.util.List;

@Path("/messages")
public class MessagesEndPoint {

    @Inject
    private DaoMessage daoMessage;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> getAllMessages(@QueryParam("idChat") long idChat, @QueryParam("idUser") long idUser) throws SQLException {
        daoMessage.updateLastRead(idChat, idUser);
        return daoMessage.getAllMessages(idChat);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void sendMessage(Message message) throws SQLException {
        daoMessage.sendMessage(message.getMessage(), message.getIdChat(), message.getUser().getId_user());
        daoMessage.updateLastRead(message.getIdChat(), message.getUser().getId_user());
    }

}
