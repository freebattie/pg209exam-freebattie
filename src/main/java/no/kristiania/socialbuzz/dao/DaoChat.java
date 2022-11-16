package no.kristiania.socialbuzz.dao;

import jakarta.inject.Inject;
import no.kristiania.socialbuzz.dto.Chat;
import no.kristiania.socialbuzz.dto.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class DaoChat {

    private final Connection connection;

    @Inject
    public DaoChat(Connection connection) {
        this.connection = connection;
    }

    public List<Chat> getAllChats(long idUser) throws SQLException {
        Map<Chat, User> usersChats = new HashMap<>();

        var sqlGetChats = """
                    SELECT u.id_chat, c.title
                    FROM userschats u
                    JOIN chats c on u.id_chat = c.id_chat
                    WHERE u.id_user = ?;
                """;

        var sqlGetUsers = """
                    SELECT us.id_user, username
                    FROM userschats us
                    JOIN users u on u.id_user = us.id_user
                    WHERE us.id_chat = ?;
                """;

        try (var statementChats = connection.prepareStatement(sqlGetChats)) {
            statementChats.setLong(1, idUser);
            var resultChats = statementChats.executeQuery();

            while (resultChats.next()) {
                var tmpChat = new Chat();
                tmpChat.setId_chat(resultChats.getLong(1));
                tmpChat.setTitle(resultChats.getString(2));

                try (var statementUsers = connection.prepareStatement(sqlGetUsers)) {
                    statementUsers.setInt(1, resultChats.getInt(1));
                    var resultUsers = statementUsers.executeQuery();

                    while (resultUsers.next()) {
                        if (resultUsers.getLong(1) == idUser) continue;

                        var tmpUser = new User();
                        tmpUser.setId_user(resultUsers.getLong(1));
                        tmpUser.setUsername(resultUsers.getString(2));

                        if (usersChats.containsKey(tmpChat)) {
                            usersChats.get(tmpChat).setUsername(tmpChat.getTitle());

                        } else {
                            usersChats.put(tmpChat, tmpUser);
                        }
                    }
                }

            }

        }

        List<Chat> chats = new ArrayList<>();
        for (var chat : usersChats.entrySet()) {
            var tmp = chat.getKey();
            tmp.setTitle(chat.getValue().getUsername());
            chats.add(tmp);
        }
        Collections.sort(chats);
        return chats;
    }

    public void makeNewChat(List<Integer> users, String title) throws SQLException {
        int idChat;

        var sqlMakeChat = """
                INSERT INTO chats (title)
                VALUES (?)
                """;
        try (var statement = connection.prepareStatement(sqlMakeChat, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, Objects.requireNonNullElse(title, ""));
            statement.executeUpdate();

            var keys = statement.getGeneratedKeys();
            keys.next();
            idChat = keys.getInt(1);
        }

        for (var id : users) {
            var sql = """
                INSERT INTO userschats (id_user, id_chat)
                VALUES (?, ?);
                """;

            try (var statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
                statement.setInt(2, idChat);
                statement.executeUpdate();
            }
        }

    }

    public List<String> getAllUsersInChat(long idChat) throws SQLException {
        var sql = """
                SELECT username
                FROM userschats c
                JOIN users u on c.id_user = u.id_user
                WHERE id_chat = ?;
                """;

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, idChat);
            var result = statement.executeQuery();

            List<String> usernames = new ArrayList<>();
            while (result.next()) {
                usernames.add(result.getString(1));
            }

            return usernames;
        }
    }

}

