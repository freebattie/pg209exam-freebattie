package no.kristiania.socialbuzz.endpoints;

import com.google.gson.Gson;
import jakarta.json.Json;
import no.kristiania.socialbuzz.SocialBuzzServer;
import no.kristiania.socialbuzz.dao.DaoUser;
import no.kristiania.socialbuzz.db.InMemoryDataSource;
import no.kristiania.socialbuzz.dto.Email;
import no.kristiania.socialbuzz.dto.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
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
    public void removeEmailTest() throws SQLException, IOException, InterruptedException {

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
    public void addEmailTest() throws SQLException, IOException, InterruptedException {

        var user = daoUser.getUserById(4);
        var gson = new Gson();
        var userJson = gson.toJson(user.getId_user());
        var connection = openConnection("/api/users/emails/");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-type", "application/json");
        connection.getOutputStream().write(userJson
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

    @Test
    public void createNewUserTest() throws IOException {
        var user = new User();
        user.setUsername("Testuser");
        user.setName("Testerson");
        user.setTlf("74857485");

        var email1 = new Email();
        var email2 = new Email();
        email1.setEmail("test@test.no");
        email2.setEmail("test@gmail.com");
        user.setEmails(email1);
        user.setEmails(email2);

        var gson = new Gson();
        var string = gson.toJson(user);

        var postConnection = openConnection("/api/users");
        postConnection.setRequestMethod("POST");
        postConnection.setDoOutput(true);
        postConnection.setRequestProperty("Content-type", "application/json");
        postConnection.getOutputStream().write(string.getBytes(StandardCharsets.UTF_8));

        assertThat(postConnection.getResponseCode())
                .as("Check if POST worked")
                .isEqualTo(204);
    }

    private HttpURLConnection openConnection(String spec) throws IOException {
        return (HttpURLConnection) new URL(server.getURL(), spec).openConnection();
    }

}
