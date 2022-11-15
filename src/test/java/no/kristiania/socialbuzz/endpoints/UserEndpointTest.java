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
                .as("Server response code is 200")
                .isEqualTo(200);

        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains(users.get(0).getUsername());

    }


    private HttpURLConnection openConnection(String spec) throws IOException {
        return (HttpURLConnection) new URL(server.getURL(), spec).openConnection();
    }

}
