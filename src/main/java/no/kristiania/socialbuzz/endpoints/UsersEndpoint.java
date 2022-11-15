package no.kristiania.socialbuzz.endpoints;

import com.google.gson.Gson;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.socialbuzz.dao.Email;
import no.kristiania.socialbuzz.dao.User;
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
    public void editUser(String user) throws SQLException {
        Gson gson = new Gson();

        var test = gson.fromJson(user,User.class);

        daoUser.EditUser(test);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserById(@PathParam("id") Long id) throws SQLException {
        return daoUser.getUser(id);
    }
    @GET
    @Path("/emails")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public long getEmailsByUserId(@QueryParam("id") Long id) throws SQLException {
        return daoUser.getLastEmailId(id);
    }
    @POST
    @Path("/emails")
    @Consumes(MediaType.APPLICATION_JSON)
    public void AddUserByEmail(User user) throws SQLException {
         daoUser.addEmail(user.getId_user());
    }
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/emails/{id}")
    public void deleteEmailByID(@PathParam("id")long id) throws SQLException {
        daoUser.removeEmail(id);;
    }

}
