package no.kristiania.socialbuzz.dao;

import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DaoMessage {

    private final Connection connection;

    @Inject
    public DaoMessage(Connection connection) {
        this.connection = connection;
    }

    public void updateLastRead(long idChat, long idUser) throws SQLException {

//        Delete current status on lastRead
        var sqlDelete = """
                DELETE FROM lastRead
                WHERE id_chat = ? AND id_user = ?;
                """;

        try (var statement = connection.prepareStatement(sqlDelete)) {
            statement.setLong(1, idChat);
            statement.setLong(2, idUser);
            statement.executeUpdate();
        }

//        Get newest message ID
        long idMessage;
        var sqlIdMessage = """
                SELECT id_message
                FROM messages
                WHERE id_chat = ?
                ORDER BY id_message DESC
                OFFSET 0 ROW
                FETCH NEXT 1 ROW ONLY;
                """;

        try (var statement = connection.prepareStatement(sqlIdMessage)) {
            statement.setLong(1, idChat);
            var result = statement.executeQuery();

            result.next();
            idMessage = result.getLong(1);
        }


//        Update what the newest message is.
        var sqlUpdate = """
                INSERT INTO lastRead (id_chat, id_message, id_user)
                VALUES (?, ?, ?);
                """;

        try (var statement = connection.prepareStatement(sqlUpdate)) {
            statement.setLong(1, idChat);
            statement.setLong(2, idMessage);
            statement.setLong(3, idUser);
            statement.executeUpdate();
        }


    }

    public List<Message> getAllMessages(long idChat) throws SQLException {

        var sqlMessages = """
                SELECT id_message, message, timestamp
                FROM messages m
                JOIN users u on m.id_user = u.id_user
                WHERE id_chat = ?;
                """;

        var sqlLastRead = """
                SELECT username
                FROM lastRead l
                JOIN users u on l.id_user = u.id_user
                WHERE id_chat = ? AND id_message = ?;
                """;

        try (var statementMessages = connection.prepareStatement(sqlMessages)) {
            statementMessages.setLong(1, idChat);
            var resultMessages = statementMessages.executeQuery();

            List<Message> messages = new ArrayList<>();

            while (resultMessages.next()) {
                var tmpMessage = new Message();
                List<User> users = new ArrayList<>();

                try (var statementUsers = connection.prepareStatement(sqlLastRead)) {
                    statementUsers.setLong(1, idChat);
                    statementUsers.setLong(2, resultMessages.getLong(1));
                    var resultUsers = statementUsers.executeQuery();

                    while (resultUsers.next()) {
                        var tmpUser = new User();
                        tmpUser.setUsername(resultUsers.getString(1));
                        users.add(tmpUser);
                    }
                }

                tmpMessage.setIdMessage(resultMessages.getLong(1));
                tmpMessage.setMessage(resultMessages.getString(2));
                // TODO: 12.11.2022 implement long -> (String) date
                tmpMessage.setMessage(resultMessages.getString(3));
                tmpMessage.setLastReads(users);
                messages.add(tmpMessage);
            }

            return messages;
        }
    }

    public void sendMessage(Message message) throws SQLException {
        var sqlMessage = """
                INSERT INTO messages (message, timestamp, id_chat, id_user)
                VALUES (?, ?, ?, ?);
                """;

        try (var statement = connection.prepareStatement(sqlMessage)) {
            statement.setString(1, message.getMessage());
            statement.setLong(2, Instant.now().getEpochSecond());
            statement.setLong(3, message.getIdChat());
            statement.setLong(4, message.getUser().id_user);
            statement.executeUpdate();
        }
    }

}
