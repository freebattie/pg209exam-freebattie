package no.kristiania.socialbuzz.dao;

import no.kristiania.socialbuzz.db.InMemoryDataSource;
import no.kristiania.socialbuzz.dto.Chat;
import no.kristiania.socialbuzz.dto.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DaoChatTest {

    private Connection connection;
    private DaoChat dao;

    @BeforeEach
    public void h2DbSetup() throws SQLException {
        this.connection = InMemoryDataSource.createTestDataSource().getConnection();
        connection.setAutoCommit(false);
        this.dao = new DaoChat(connection);
    }

    @AfterEach
    public void rollbackTest() throws SQLException {
        connection.rollback();
    }

    @Test
    public void getAllChatsFromUserId() throws SQLException {
        var result = dao.getAllChats(1);

        assertEquals(4, result.size());

        assertThat(result.get(0).getTitle())
                .as("Check that Chat 1 title is SecretMan")
                .isEqualTo("SecretMan");

        assertThat(result.get(0).getId_chat())
                .as("Check that Chat 1 ID is 1")
                .isEqualTo(1);

        assertThat(result.get(3).getTitle())
                .as("Check that Group chat 4 title is Chat 4")
                .isEqualTo("Chat 4");

        assertThat(result.get(3).getId_chat())
                .as("Check that Chat 4 ID is 4")
                .isEqualTo(4);
    }

    @Test
    public void makeNewOneToOneChat() throws SQLException {
        var daoUser = new DaoUser(connection);
        var user1 = daoUser.getUserById(1);
        var user2 = daoUser.getUserById(2);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

       // dao.makeNewChat(users, "chat from test");
        var chats = dao.getAllChats(1);
        Chat testChat = new Chat();

        for (var chat : chats) {
            if (chat.getTitle().equals("chat from test") || chat.getTitle().equals(user2.getUsername())) {
                testChat = chat;
            }
        }

        assertThat(testChat.getTitle())
                .as("Check that new Chat title is username from user2")
                .isEqualTo(user2.getUsername());
    }

    @Test
    public void makeNewGroupChat() throws SQLException {
        var daoUser = new DaoUser(connection);
        var user1 = daoUser.getUserById(1);
        var user2 = daoUser.getUserById(2);
        var user3 = daoUser.getUserById(3);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);

        //dao.makeNewChat(users, "Group chat number 2");
        var chats = dao.getAllChats(1);
        Chat testChat = new Chat();

        for (var chat : chats) {
            if (chat.getTitle().equals("Group chat number 2")) {
                testChat = chat;
            }
        }

        assertThat(testChat.getTitle())
                .as("Check that new Chat title is Group chat number 2")
                .isEqualTo("Group chat number 2");
    }

}
