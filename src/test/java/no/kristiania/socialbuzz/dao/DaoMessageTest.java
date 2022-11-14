package no.kristiania.socialbuzz.dao;

import no.kristiania.socialbuzz.db.InMemoryDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class DaoMessageTest {

    private DaoMessage dao;

    @BeforeEach
    public void h2DbSetup() throws SQLException {
        var con = InMemoryDataSource.createTestDataSource();
        this.dao = new DaoMessage(con.getConnection());
    }

    @Test
    public void sendOneMessage() throws SQLException {
        var message = new Message();
        message.setMessage("This is a test message!");
        message.setIdChat(1);

        var tmpUser = new User();
        tmpUser.setId_user(1);
        message.setUser(tmpUser);


        dao.sendMessage(message);
        var messages = dao.getAllMessages(1, 1);

        assertThat(messages.get(messages.size()-1).getMessage())
                .as("Check last message is same")
                .isEqualTo("This is a test message!");
    }

    @Test
    public void updateLastRead() throws SQLException {
        var messages = dao.getAllMessages(4, 2);

        assertThat(messages.get(2).lastReads.get(0).getIdUser())
                .as("Check if user 1 has lastRead at message 7")
                .isEqualTo(1);

        dao.updateLastRead(4, 1);
        messages = dao.getAllMessages(4, 2);

        assertThat(messages.get(4).lastReads.get(0).getIdUser())
                .as("Check if user 1 has lastRead at message 7")
                .isEqualTo(1);
    }


}


