package no.kristiania.socialbuzz.endpoints;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.socialbuzz.dao.User;
import no.kristiania.socialbuzz.dao.DaoUser;

import java.sql.SQLException;
import java.util.List;


@Path("/user-login")
public class UserLoginEndpoint {

    @Inject
    private DaoUser daoUser;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUserLogin() throws SQLException {
        return daoUser.getAllUserLogin();
    }

}
