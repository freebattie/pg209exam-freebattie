package no.kristiania.socialbuzz.dao;

import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DaoMessage {

    private final Connection connection;

    @Inject
    public DaoMessage(Connection connection) {
        this.connection = connection;
    }

    public void updateLastRead(long idChat, long idUser) throws SQLException {
        var sql = """
                SELECT id_message
                FROM messages
                WHERE id_chat = ? 
                """;

        try (var statment = connection.prepareStatement(sql)) {

        }
    }

    public List<Message> getAllMessages(long idChat) throws SQLException {


        return null;
    }

    public void sendMessage(String message) {

    }

}
