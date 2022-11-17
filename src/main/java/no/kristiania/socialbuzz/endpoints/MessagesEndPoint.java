package no.kristiania.socialbuzz.endpoints;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.socialbuzz.dao.DaoMessage;
import no.kristiania.socialbuzz.dto.Message;

import java.sql.SQLException;
import java.util.List;

@Path("/messages")
public class MessagesEndPoint {

    @Inject
    private DaoMessage daoMessage;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> getAllMessages(@QueryParam("idChat") long idChat, @QueryParam("idUser") long idUser) throws SQLException {
        return daoMessage.getAllMessages(idChat, idUser);
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateLastRead(@QueryParam("idChat") long idChat, @QueryParam("idUser") long idUser) throws SQLException {
        System.out.println(idUser);
        daoMessage.updateLastRead(idChat, idUser);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void sendMessage(Message message) throws SQLException {
        daoMessage.sendMessage(message);
    }

}
