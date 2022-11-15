package no.kristiania.socialbuzz.endpoints;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.socialbuzz.dto.User;
import no.kristiania.socialbuzz.dao.DaoUser;

import java.sql.SQLException;
import java.util.List;


@Path("/users")
public class UsersEndpoint {

    @Inject
    private DaoUser daoUser;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUserLogin() throws SQLException {
        return daoUser.getAllUserLogin();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void editUser(User user) throws SQLException {
        daoUser.EditUser(user);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserById(@PathParam("id") Long id) throws SQLException {
        return daoUser.getUserById(id);
    }

}
