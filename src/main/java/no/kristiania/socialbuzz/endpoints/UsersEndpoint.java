package no.kristiania.socialbuzz.endpoints;

import com.google.gson.Gson;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.socialbuzz.dao.DaoUser;
import no.kristiania.socialbuzz.dto.User;

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
    public void editUser(String userJson) throws SQLException {
        Gson gson = new Gson();
        var user = gson.fromJson(userJson,User.class);
        daoUser.editUser(user);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createNewUser(String userJson) throws SQLException {
        Gson gson = new Gson();
        var user = gson.fromJson(userJson,User.class);
        daoUser.createNewUser(user);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserById(@PathParam("id") Long id) throws SQLException {
        return daoUser.getUserById(id);
    }

    @GET
    @Path("/emails")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public long getLastEmailId(@QueryParam("id") Long id) throws SQLException {
        return daoUser.getLastEmailId(id);
    }

    @POST
    @Path("/emails")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addEmailByUserId(User user) throws SQLException {
         daoUser.addEmailByUserId(user.getId_user());
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/emails/{id}")
    public void deleteEmailByEmailId(@PathParam("id")long id) throws SQLException {
        daoUser.deleteEmailByEmailId(id);
    }

}
