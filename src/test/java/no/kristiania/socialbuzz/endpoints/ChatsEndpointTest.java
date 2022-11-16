package no.kristiania.socialbuzz.endpoints;

import com.google.gson.Gson;
import no.kristiania.socialbuzz.SocialBuzzServer;
import no.kristiania.socialbuzz.dao.DaoChat;
import no.kristiania.socialbuzz.db.InMemoryDataSource;
import no.kristiania.socialbuzz.dto.Chat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ChatsEndpointTest {

    private SocialBuzzServer server;
    private Connection connection;
    private DaoChat daoChat;

    @BeforeEach
    public void h2DbSetup() throws Exception {
        var dataSource = InMemoryDataSource.createTestDataSource();
        this.server = new SocialBuzzServer(0, dataSource);
        this.connection = dataSource.getConnection();
        server.start();

        connection.setAutoCommit(false);
        this.daoChat = new DaoChat(connection);
    }

    @AfterEach
    public void rollbackTest() throws SQLException {
        connection.rollback();
    }

    @Test
    public void getAllChatsByUserId() throws SQLException, IOException {
        var chats = daoChat.getAllChats(1);

        var connection = openConnection("/api/chats/1");
        connection.setRequestMethod("GET");

        assertThat(connection.getResponseCode())
                .as("Check if GET work")
                .isEqualTo(200);

        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains(chats.get(0).getTitle());
    }

    @Test
    public void makeNewChatTest() throws IOException {
        var newChat = new Chat();
        newChat.setTitle("This tittle is a test");

        List<Integer> userIdList = new ArrayList<>();
        userIdList.add(2);
        userIdList.add(3);
        userIdList.add(4);
        newChat.setUserIdList(userIdList);

        var gson = new Gson();
        var string = gson.toJson(newChat);

        var postConnection = openConnection("/api/chats");
        postConnection.setRequestMethod("POST");
        postConnection.setDoOutput(true);
        postConnection.setRequestProperty("Content-type", "application/json");
        postConnection.getOutputStream().write(string.getBytes(StandardCharsets.UTF_8));

        assertThat(postConnection.getResponseCode())
                .as("Check if POST work")
                .isEqualTo(204);


        var getConnection = openConnection("/api/chats/4");
        getConnection.setRequestMethod("GET");

        assertThat(getConnection.getResponseCode())
                .as("Check if GET work")
                .isEqualTo(200);

        assertThat(getConnection.getInputStream())
                .as("Check if new chat tittle is in the list")
                .asString(StandardCharsets.UTF_8)
                .contains(newChat.getTitle());
    }

    private HttpURLConnection openConnection(String spec) throws IOException {
        return (HttpURLConnection) new URL(server.getURL(), spec).openConnection();
    }

}
