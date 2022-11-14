package no.kristiania.socialbuzz.dao;

import no.kristiania.socialbuzz.db.InMemoryDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DaoChatTest {

    private DaoChat dao;

    @BeforeEach
    public void h2DbSetup() throws SQLException {
        var con = InMemoryDataSource.createTestDataSource();
        this.dao = new DaoChat(con.getConnection());
    }

    @Test
    public void getAllChatsFromUserId() throws SQLException {
        var result = dao.getAllChats(1);

        assertEquals(5, result.size());

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
        var user1 = new User();
        user1.setId_user(1);
        var user2 = new User();
        user2.setId_user(4);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        dao.makeNewChat(users, "chat from test");
        var result = dao.getAllChats(1);

        assertThat(result.get(4).getTitle())
                .as("Check that new Chat title is Randomperson2")
                .isEqualTo("NotSecretman");
    }

    @Test
    public void makeNewGroupChat() throws SQLException {
        var user1 = new User();
        user1.setId_user(1);
        var user2 = new User();
        user2.setId_user(2);
        var user3 = new User();
        user3.setId_user(3);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);

        dao.makeNewChat(users, "Group chat number 2");
        var result = dao.getAllChats(1);

        assertThat(result.get(5).getTitle())
                .as("Check that new Chat title is Group chat number 2")
                .isEqualTo("Group chat number 2");
    }

}
