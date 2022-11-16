package no.kristiania.socialbuzz.endpoints;

import com.google.gson.Gson;
import jakarta.json.Json;
import no.kristiania.socialbuzz.SocialBuzzServer;
import no.kristiania.socialbuzz.dao.DaoUser;
import no.kristiania.socialbuzz.db.InMemoryDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class UserEndpointTest {

    private SocialBuzzServer server;
    private Connection connection;
    private DaoUser daoUser;

    @BeforeEach
    public void h2DbSetup() throws Exception {
        var dataSource = InMemoryDataSource.createTestDataSource();
        this.server = new SocialBuzzServer(0, dataSource);
        this.connection = dataSource.getConnection();
        server.start();

        connection.setAutoCommit(false);
        this.daoUser = new DaoUser(connection);
    }

    @AfterEach
    public void rollbackTest() throws SQLException {
        connection.rollback();
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
        var user = daoUser.getUserById(4);
        user.setUsername("TestUser");
        user.setName("Ola Nordmann");
        user.setTlf("99887766");

//        Make Json file out of user
        var gson = new Gson();
        var userJson = gson.toJson(user);

//        Send changes to server
        var preConnection = openConnection("/api/users");
        preConnection.setRequestMethod("PUT");
        preConnection.setRequestProperty("Content-Type", "application/json");
        preConnection.setDoOutput(true);
        preConnection.getOutputStream().write(
                userJson.getBytes(StandardCharsets.UTF_8)
        );

//        Confirms that PUT executed
        assertThat(preConnection.getResponseCode())
                .as(preConnection.getResponseMessage())
                .isEqualTo(204);

//        Get edited user back and compare
        var connection = openConnection("/api/users/4");
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

    @Test
    public void GetLastEmailIdTest() throws SQLException, IOException {
        var user = daoUser.getUserById(4);
        var emails = user.getEmails();
        var last = emails.get(emails.size() - 1).getId();
        var connection = openConnection("/api/users/emails?id=" + user.getId_user());
        connection.setRequestMethod("GET");

        assertThat(connection.getResponseCode())
                .as("Server respond code is 200")
                .isEqualTo(200);

        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .as("Check that last email is same as users last email")
                .isEqualTo(last.toString());

    }

    @Test
    public void removeEmailTest() throws SQLException, IOException {
        var user = daoUser.getUserById(4);
        var emailId = user.getEmails().get(0).getId();
        var connection = openConnection("/api/users/emails/" + emailId);
        connection.setRequestMethod("DELETE");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-type", "application/json");
        assertThat(connection.getResponseCode())
                .as("Check if DELETE worked")
                .isEqualTo(204);
        var newUser = daoUser.getUserById(4);
        // Check that user is the expected user
        assertThat(newUser.getEmails().size())
                .as("Check that size of emails has changed")
                .isNotEqualTo(user.getEmails().size());
    }

    @Test
    public void addEmailTest() throws SQLException, IOException {
        var user = daoUser.getUserById(4);

        var connection = openConnection("/api/users/emails/");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-type", "application/json");
        connection.getOutputStream().write(Json.createObjectBuilder()
                .add("id_user", user.getId_user())
                .build()
                .toString()
                .getBytes(StandardCharsets.UTF_8));
        assertThat(connection.getResponseCode())
                .as("Check if POST worked")
                .isEqualTo(204);

        var newUser = daoUser.getUserById(4);
        // Check that user is the expected user
        assertThat(newUser.getEmails().size())
                .as("Check that emails has increased")
                .isGreaterThan(user.getEmails().size());
    }

    private HttpURLConnection openConnection(String spec) throws IOException {
        return (HttpURLConnection) new URL(server.getURL(), spec).openConnection();
    }

}
