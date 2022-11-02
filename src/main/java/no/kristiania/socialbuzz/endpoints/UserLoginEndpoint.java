package no.kristiania.socialbuzz.endpoints;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.socialbuzz.db.DaoUser;

import java.sql.SQLException;
import java.util.Map;


@Path("/user-login")
public class UserLoginEndpoint {

    @Inject
    private DaoUser daoUser;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<Integer, String> getAllUserLogin() throws SQLException {
        return daoUser.getAllUserLogin();
    }

}
