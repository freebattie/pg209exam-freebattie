package no.kristiania.socialbuzz.dao;

import no.kristiania.socialbuzz.db.InMemoryDataSource;
import no.kristiania.socialbuzz.dto.Message;
import no.kristiania.socialbuzz.dto.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class DaoMessageTest {

    private Connection connection;
    private DaoMessage dao;


    @BeforeEach
    public void h2DbSetup() throws SQLException {
        this.connection = InMemoryDataSource.createTestDataSource().getConnection();
        connection.setAutoCommit(false);
        this.dao = new DaoMessage(connection);
    }

    @AfterEach
    public void rollbackTest() throws SQLException {
        connection.rollback();
    }


    @Test
    public void updateLastRead() throws SQLException {
        var messages = dao.getAllMessages(4, 3);
        var lastMessage = new Message();

        for (var message : messages) {
            if (lastMessage.getIdMessage() < message.getIdMessage()) {
                lastMessage.setIdMessage(message.getIdMessage());
                lastMessage.setLastReads(message.getLastReads());
            }
        }

        assertThat(lastMessage.getLastReads().size())
                .as("Confirm only us zero out of two timestamp is on last message")
                .isEqualTo(0);

        dao.updateLastRead(4, 1);
        dao.updateLastRead(4, 2);
        dao.updateLastRead(4, 3);
        messages = dao.getAllMessages(4, 3);
        lastMessage = new Message();

        for (var message : messages) {
            if (lastMessage.getIdMessage() < message.getIdMessage()) {
                lastMessage.setIdMessage(message.getIdMessage());
                lastMessage.setLastReads(message.getLastReads());
            }
        }

        assertThat(lastMessage.getLastReads().size())
                .as("Confirm only us two out of two timestamp is on last message")
                .isEqualTo(2);
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

}