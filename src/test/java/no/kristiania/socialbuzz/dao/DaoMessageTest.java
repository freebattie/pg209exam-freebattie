package no.kristiania.socialbuzz.dao;

import no.kristiania.socialbuzz.db.InMemoryDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class DaoMessageTest {

    private DaoMessage daoMessage;
    private DaoChat daoChat;

    @BeforeEach
    public void h2DbSetup() throws SQLException {
        var con = InMemoryDataSource.createTestDataSource();
        this.daoMessage = new DaoMessage(con.getConnection());
        this.daoChat = new DaoChat(con.getConnection());
    }

    @Test
    public void sendOneMessage() throws SQLException {

    }


}


