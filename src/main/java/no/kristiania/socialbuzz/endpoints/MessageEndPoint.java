package no.kristiania.socialbuzz.endpoints;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.socialbuzz.dao.DaoMessage;
import no.kristiania.socialbuzz.dao.Message;

import java.sql.SQLException;
import java.util.List;

public class MessageEndPoint {

    @Inject
    private DaoMessage daoMessage;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> getAllMessages(int idChat) throws SQLException {
        return daoMessage.getAllMessages(idChat);
    }

}
