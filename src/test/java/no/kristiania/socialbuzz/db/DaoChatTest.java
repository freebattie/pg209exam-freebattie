package no.kristiania.socialbuzz.db;

import no.kristiania.socialbuzz.dao.DaoChat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class DaoChatTest {

    private DaoChat dao;

    @BeforeEach
    public void h2DbSetup() throws SQLException {
        var con = InMemoryDataSource.createTestDataSource();
        this.dao = new DaoChat(con.getConnection());
    }

    @Test
    public void getAllChatsFromUserId() {

    }

}
