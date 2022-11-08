package no.kristiania.socialbuzz.db;

import no.kristiania.socialbuzz.dao.DaoChat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

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

        assertEquals(4, result.size());

        assertThat(result.get(0).getTitle())
                .as("Check that Chat 1 title is SecretMan")
                .isEqualTo("SecretMan");

        assertThat(result.get(0).getId_chat())
                .as("Check that Chat 1 ID is 1")
                .isEqualTo(1);

    }

}
