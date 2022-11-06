package no.kristiania.socialbuzz.dao;

import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoChat {

    private final Connection connection;

    @Inject
    public DaoChat(Connection connection) {
        this.connection = connection;
    }

    public List<Chat> getAllChats(int idUser) throws SQLException {

        var sql = """
                    SELECT chat.id_chat, title, id_message
                    FROM [users-chats]
                    JOIN chats chat on [users-chats].id_chat = chat.id_chat
                    WHERE id_user = ?;
                """;

        try (var statement = connection.prepareStatement(sql)) {
            List<Chat> chats = new ArrayList<>();

            statement.setInt(1, idUser);
            var result = statement.executeQuery();

            while (result.next()) {
                var tmpChat = new Chat();
                tmpChat.setId_chat(result.getLong(1));
                tmpChat.setTitle(result.getString(2));
                chats.add(tmpChat);
            }

            return chats;
        }

    }

}
