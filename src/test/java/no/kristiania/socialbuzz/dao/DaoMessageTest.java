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
        var messages = dao.getAllMessages(1, 1);

        for (var message : messages) {
            if (message.lastReads.size() > 0){
                System.out.println(message.getMessage() + " " + message.getLastReads().get(0).timestamp +" "+ message.lastReads.get(0).getUsername());
            }
        }
    }


}


