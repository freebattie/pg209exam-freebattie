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
    public void updateLastRead() throws SQLException {
        var messages = dao.getAllMessages(4, 2);
        var lastMessage = new Message();

        for (var message : messages) {
            if (lastMessage.getIdMessage() < message.getIdMessage()) {
                lastMessage.setIdMessage(message.getIdMessage());
                lastMessage.setLastReads(message.getLastReads());
            }
        }

        assertThat(lastMessage.getLastReads().size())
                .as("Confirm only us two out of three user read last message")
                .isEqualTo(0);

        dao.updateLastRead(4, 1);
        messages = dao.getAllMessages(4, 2);
        lastMessage = new Message();

        for (var message : messages) {
            if (lastMessage.getIdMessage() < message.getIdMessage()) {
                lastMessage.setIdMessage(message.getIdMessage());
                lastMessage.setLastReads(message.getLastReads());
            }
        }

        assertThat(lastMessage.getLastReads().size())
                .as("Confirm only us three out of three user read last message")
                .isEqualTo(1);

        assertThat(lastMessage.getLastReads().get(0).getUsername())
                .as("Check that it is Karigirl who have updated her lastRead")
                .isEqualTo("Karigirl");

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