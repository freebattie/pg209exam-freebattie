package no.kristiania.socialbuzz.endpoints;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.socialbuzz.User;
import no.kristiania.socialbuzz.db.DaoUser;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@Path("/user-login")
public class UserLoginEndpoint {

    @Inject
    private DaoUser daoUser;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUserLogin() throws SQLException {
        return daoUser.getAllUserLogin();
//        var array = new String[2];
//        array[0] = "Snorre";
//        array[1] = "Bjarte";
//        return array;
    }

}
