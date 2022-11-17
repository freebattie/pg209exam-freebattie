package no.kristiania.socialbuzz.endpoints;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.json.Json;
import no.kristiania.socialbuzz.SocialBuzzServer;
import no.kristiania.socialbuzz.dao.DaoChat;
import no.kristiania.socialbuzz.dao.DaoMessage;
import no.kristiania.socialbuzz.dao.DaoUser;
import no.kristiania.socialbuzz.db.InMemoryDataSource;

import no.kristiania.socialbuzz.dto.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;

import java.net.URL;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;

public class MessagesEndPointTest {

    private SocialBuzzServer server;
    private Connection connection;
    private DaoMessage daoMessages;
    private DaoUser daoUser;
    private DaoChat daoChat;

    @BeforeEach
    public void h2DbSetup() throws Exception {
        var dataSource = InMemoryDataSource.createTestDataSource();
        this.server = new SocialBuzzServer(0, dataSource);
        this.connection = dataSource.getConnection();
        server.start();

        connection.setAutoCommit(false);
        this.daoMessages = new DaoMessage(connection);
        this.daoUser = new DaoUser(connection);
        this.daoChat = new DaoChat(connection);
    }


    @AfterEach
    public void rollbackTest() throws SQLException {
        connection.rollback();
    }

    @Test
    public void UpdateLastReadTest() throws IOException, SQLException {
        //http://localhost:8080/users?page=1&limit=50
        var userOne = daoUser.getUserById(1);
        var userOneChats = daoChat.getAllChats(userOne.getId_user());

        var getChatId = userOneChats.get(3).getId_chat();

        var userWithLastMessage = daoUser.getUserById(3);
        var userThreeId = userWithLastMessage.getId_user();

        var usersSeenLastMsg = daoMessages.getAllMessages(getChatId, userThreeId);


        var postConnection = openConnection("/api/messages/update?idChat=" + getChatId + "&idUser=" + userOne.getId_user());
        postConnection.setRequestMethod("POST");
        postConnection.setDoOutput(true);
        postConnection.setRequestProperty("Content-type", "application/json");

        assertThat(postConnection.getResponseCode())
                .as("Check if POST worked")
                .isEqualTo(204);


        var getConnection = openConnection("/api/messages?idChat=" + getChatId + "&idUser" + userThreeId);
        getConnection.setRequestMethod("GET");
        List<Message> messages = createListOfClassType(getConnection);
        Message message = getMessageFromList(messages, 3);

        //Check that last read has moved for user 1
        assertThat(message.getLastReads())
                .as("Check if POST worked")
                .usingRecursiveComparison()
                .isNotEqualTo(usersSeenLastMsg.get(3).getLastReads());
    }

    @Test
    public void SendMessageTest() throws IOException, SQLException {
        //http://localhost:8080/users?page=1&limit=50
        var userOne = daoUser.getUserById(1);
        var userOneChats = daoChat.getAllChats(userOne.getId_user());

        var getChatId = userOneChats.get(3).getId_chat();


        var usersSeenLastMsg = daoMessages.getAllMessages(getChatId, userOne.getId_user());
        var postConnection = openConnection("/api/messages/");
        postConnection.setRequestMethod("POST");
        postConnection.setDoOutput(true);
        postConnection.setRequestProperty("Content-type", "application/json");
        postConnection.getOutputStream().write(Json.createObjectBuilder()
                .add("idChat", getChatId)
                .add("message", "test")
                .add("user", userOne.getId_user())
                .build()
                .toString()
                .getBytes(StandardCharsets.UTF_8));

        assertThat(postConnection.getResponseCode())
                .as("Check if POST worked")
                .isEqualTo(204);

        var getConnection = openConnection("/api/messages?idChat=" + getChatId + "&idUser" + userOne.getId_user());
        getConnection.setRequestMethod("GET");
        List<Message> messages = createListOfClassType(getConnection);

        assertThat(messages).as("Check that messages are bigger")
                .hasSizeGreaterThan(usersSeenLastMsg.size());
    }


    private static Message getMessageFromList(List<Message> messages, int index) {
        var name = new Gson().toJson(messages.get(index));
        return new Gson().fromJson(name, Message.class);
    }

    private static <T> List<T> createListOfClassType(HttpURLConnection getConnection) throws IOException {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(getConnection.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line;

        while ((line = buffer.readLine()) != null) {
            builder.append(line).append("\n");
        }

        String val = builder.toString();
        buffer.close();


        Type listType = new TypeToken<ArrayList<T>>() {}.getType();
        return new Gson().fromJson(val, listType);
    }

    private HttpURLConnection openConnection(String spec) throws IOException {
        return (HttpURLConnection) new URL(server.getURL(), spec).openConnection();
    }
}
