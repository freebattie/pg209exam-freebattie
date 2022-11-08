package no.kristiania.socialbuzz.dao;

import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoMessage {

    private final Connection connection;

    @Inject
    public DaoMessage(Connection connection) {
        this.connection = connection;
    }

    public List<Message> getAllMessages(int idChat) throws SQLException {
        var sql = """
                    SELECT *
                    FROM messages
                    WHERE id_chat = ?;
                """;

        try (var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idChat);
            var result = statement.executeQuery();

            List<Message> messages = new ArrayList<>();
            while (result.next()) {
                var tmpMessage = new Message();
                tmpMessage.setIdMessage(result.getLong(1));
                tmpMessage.setMessage(result.getString(2));
                tmpMessage.setIdChat(result.getLong(3));
                tmpMessage.setIdUser(result.getLong(4));
                messages.add(tmpMessage);
            }

            return messages;
        }
    }

}
