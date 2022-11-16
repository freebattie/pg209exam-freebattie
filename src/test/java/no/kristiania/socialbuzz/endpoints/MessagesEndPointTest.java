package no.kristiania.socialbuzz.endpoints;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import no.kristiania.socialbuzz.SocialBuzzServer;
import no.kristiania.socialbuzz.dao.DaoChat;
import no.kristiania.socialbuzz.dao.DaoMessage;
import no.kristiania.socialbuzz.dao.DaoUser;
import no.kristiania.socialbuzz.db.InMemoryDataSource;

import no.kristiania.socialbuzz.dto.LastRead;
import no.kristiania.socialbuzz.dto.Message;
import no.kristiania.socialbuzz.dto.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;

import java.net.URL;

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
    public void  UpdateLastReadTest() throws IOException, SQLException {
        //http://localhost:8080/users?page=1&limit=50
        var userOne = daoUser.getUserById(1);
        var userOneChats = daoChat.getAllChats(userOne.getId_user());

        var getChatId = userOneChats.get(3).getId_chat();

        var userWithLastMessage = daoUser.getUserById(3);
        var userThreeId = userWithLastMessage.getId_user();


        var usersSeenLastMsg = daoMessages.getAllMessages(getChatId,userThreeId);


        var postConnection = openConnection("/api/messages/update?idChat="+getChatId+"&idUser="+userOne.getId_user());
        postConnection.setRequestMethod("POST");
        postConnection.setDoOutput(true);
        postConnection.setRequestProperty("Content-type","application/json");

        assertThat(postConnection.getResponseCode())
                .as("Check if POST worked")
                .isEqualTo(204);
        var getConnection = openConnection("/api/messages?idChat="+getChatId+"&idUser"+userThreeId);
        getConnection.setRequestMethod("GET");
        List<Message> messages = createListOfClassType(getConnection);
        Message message = getMessageFromList(messages, Message.class, 3);


        //Check that last read has moved for user 1
        assertThat(message.getLastReads())
                .as("Check if POST worked")
                .usingRecursiveComparison()
                .isNotEqualTo(usersSeenLastMsg.get(3).getLastReads());



    }

    private static Message getMessageFromList(List<Message> messages, Type type, int index) {
        var name = new Gson().toJson(messages.get(index));

        Message message = new Gson().fromJson(name, type);
        return message;
    }

    private static <T> T createClassOfType(HttpURLConnection getConnection, Type type)  throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(getConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        String val = sb.toString();
        br.close();

        T message = new Gson().fromJson(val,type);
        return message;
    }
    private static <T> List<T> createListOfClassType(HttpURLConnection getConnection)  throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(getConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        String val = sb.toString();
        br.close();

        var gson = new Gson();

        Type listType = new TypeToken<ArrayList<T>>(){}.getType();
        List<T> message = new Gson().fromJson(val,listType);
        return message;
    }

    private HttpURLConnection openConnection(String spec) throws IOException {
        return (HttpURLConnection) new URL(server.getURL(), spec).openConnection();
    }
}
