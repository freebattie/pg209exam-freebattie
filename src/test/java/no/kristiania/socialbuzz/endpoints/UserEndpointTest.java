package no.kristiania.socialbuzz.endpoints;

import no.kristiania.socialbuzz.SocialBuzzServer;
import no.kristiania.socialbuzz.dao.DaoUser;
import no.kristiania.socialbuzz.db.InMemoryDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class UserEndpointTest {

    private SocialBuzzServer server;
    private DaoUser daoUser;

    @BeforeEach
    public void h2DbSetup() throws SQLException {
        var con = InMemoryDataSource.createTestDataSource();
        this.daoUser = new DaoUser(con.getConnection());
    }

    @BeforeEach
    public void setupServer() throws Exception {
        this.server = new SocialBuzzServer(0, InMemoryDataSource.createTestDataSource());
        server.start();
    }


    @Test
    public void getAllUsersTest() throws IOException, SQLException {
        var users = daoUser.getAllUserLogin();

        var connection = openConnection("/api/users");
        connection.setRequestMethod("GET");

        assertThat(connection.getResponseCode())
                .as("Server respond code is 200")
                .isEqualTo(200);

        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains(users.get(0).getUsername());

    }

    @Test
    public void getUserByIdTest() throws SQLException, IOException {
        var user = daoUser.getUserById(1);

        var connection = openConnection("/api/users/1");
        connection.setRequestMethod("GET");

        assertThat(connection.getResponseCode())
                .as("Server respond code is 200")
                .isEqualTo(200);

        assertThat(connection.getInputStream())
                .as("Check input stream contains user1 data")
                .asString(StandardCharsets.UTF_8)
                .contains(user.getId_user().toString())
                .contains(user.getUsername())
                .contains(user.getName());
    }

    @Test
    public void editUserTest() throws SQLException, IOException {
        var user = daoUser.getUserById(1);
        user.setUsername("TestUser");
        user.setName("Ola Nordmann");
        user.setTlf("99887766");
        daoUser.EditUser(user);

        var connection = openConnection("/api/users/1");
        connection.setRequestMethod("GET");

        assertThat(connection.getResponseCode())
                .as("Server respond code is 200")
                .isEqualTo(200);

        assertThat(connection.getInputStream())
                .as("Check input stream contains edited data")
                .asString(StandardCharsets.UTF_8)
                .contains(user.getId_user().toString())
                .contains(user.getUsername())
                .contains(user.getName());
    }


    private HttpURLConnection openConnection(String spec) throws IOException {
        return (HttpURLConnection) new URL(server.getURL(), spec).openConnection();
    }

}
