package no.kristiania.socialbuzz.endpoints;

import com.google.gson.Gson;
import no.kristiania.socialbuzz.SocialBuzzServer;
import no.kristiania.socialbuzz.dao.DaoUser;
import no.kristiania.socialbuzz.db.InMemoryDataSource;
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

public class EMailStandAloneTest {
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
    @Test
    @Order(1)
    public void GetLastEmailIdTest() throws SQLException, IOException, InterruptedException {


        var connection = openConnection("/api/users/emails/");
        connection.setRequestMethod("GET");
        var gson = new Gson();
        assertThat(connection.getResponseCode())
                .as("Server respond code is 200")
                .isEqualTo(200);

        assertThat(connection.getInputStream()).asString(StandardCharsets.UTF_8).isEqualTo("13");
        var user = daoUser.getUserById(1);

        var userJson = gson.toJson(user.getId_user());
        var Postconnection = openConnection("/api/users/emails/");
        Postconnection.setRequestMethod("POST");
        Postconnection.setDoOutput(true);
        Postconnection.setRequestProperty("Content-type", "application/json");
        Postconnection.getOutputStream().write(userJson
                .getBytes(StandardCharsets.UTF_8));

        assertThat(Postconnection.getResponseCode())
                .as("Check if POST worked")
                .isEqualTo(204);


        var GetNewconnection = openConnection("/api/users/emails/");
        GetNewconnection.setRequestMethod("GET");

        assertThat(GetNewconnection.getResponseCode())
                .as("Server respond code is 200")
                .isEqualTo(200);


        assertThat(GetNewconnection.getInputStream()).asString(StandardCharsets.UTF_8)
                .as("Check that last email is same as users last email")
                .isEqualTo("11");

    }
    private HttpURLConnection openConnection(String spec) throws IOException {
        return (HttpURLConnection) new URL(server.getURL(), spec).openConnection();
    }
}
