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

    public List<Chat> getAllChats(Long idUser) throws SQLException {

        var sql = """
                SELECT chat.id_chat, title
                    FROM [userschats]
                    JOIN chats as chat on [userschats].id_chat = chat.id_chat
                    WHERE id_user = ?;
                """;

        try (var statement = connection.prepareStatement(sql)) {
            List<Chat> chats = new ArrayList<>();

            statement.setLong(1, idUser);
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

