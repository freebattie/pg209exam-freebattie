package no.kristiania.socialbuzz.dao;

import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
                INSERT INTO lastRead (id_chat, id_message, id_user, timestamp)
                VALUES (?, ?, ?, ?);
                """;

        try (var statement = connection.prepareStatement(sqlUpdate)) {
            statement.setLong(1, idChat);
            statement.setLong(2, idMessage);
            statement.setLong(3, idUser);
            statement.setLong(4, Instant.now().getEpochSecond());
            statement.executeUpdate();
        }


    }

    public List<Message> getAllMessages(long idChat, long idUser) throws SQLException {

        var sqlMessages = """
                SELECT id_message, message, timestamp, m.id_user, username
                FROM messages m
                JOIN users u on m.id_user = u.id_user
                WHERE id_chat = ?;
                """;

        var sqlLastRead = """
                SELECT username, timestamp, l.id_user
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
                List<LastRead> lastReadList = new ArrayList<>();

                try (var statementLastRead = connection.prepareStatement(sqlLastRead)) {
                    statementLastRead.setLong(1, idChat);
                    statementLastRead.setLong(2, resultMessages.getLong(1));
                    var resultLastRead = statementLastRead.executeQuery();

                    while (resultLastRead.next()) {
                        if (resultLastRead.getLong(3) == idUser) continue;
                        if (resultLastRead.getLong(3) == resultMessages.getLong(4)) continue;

                        var lastRead = new LastRead();
                        lastRead.setUsername(resultLastRead.getString(1));
                        lastRead.setTimestamp(getStringOfDateLastRead(resultLastRead.getLong(2)));
                        lastReadList.add(lastRead);
                    }
                }

//                Set id_message
                tmpMessage.setIdMessage(resultMessages.getLong(1));
//                Set message
                tmpMessage.setMessage(resultMessages.getString(2));
//                Set timestamp
                tmpMessage.setTimestamp(getStringOfDateMessage(resultMessages.getLong(3)));
//                Set lastReadList
                tmpMessage.setLastReads(lastReadList);
//                Set username
                var tmpUser = new User();
                tmpUser.setUsername(resultMessages.getString(5));
                tmpMessage.setUser(tmpUser);
                messages.add(tmpMessage);
            }

            return messages;
        }
    }

    /**
     * Saves message to database
     * @param message subparam:
     *                String message
     *                int idChat
     *                int idUser
     */
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

    private String getStringOfDateMessage(long timeThen) {
        var builder = new StringBuilder();
        var timeNow = Instant.now().getEpochSecond();
        var time = LocalDateTime.ofEpochSecond(timeThen, 0, ZoneOffset.ofHours(1));

        var midnightToday = Instant.now().truncatedTo(ChronoUnit.DAYS).getEpochSecond();
        var midnightYesterday = midnightToday - 86_400;

//        Message within one day
        if (timeThen - midnightToday > 0) {
            var format = DateTimeFormatter.ofPattern("HH:mm");
            var string = time.format(format);

            builder.append("Today at ");
            builder.append(string);
        }
//        Message within two days
        else if (timeThen - midnightYesterday > 0) {
            var format = DateTimeFormatter.ofPattern("HH:mm");
            var string = time.format(format);

            builder.append("Yesterday at ");
            builder.append(string);
        }
//        Message within one year
        else if (timeNow - timeThen < 31_536_000) {
            var format1 = DateTimeFormatter.ofPattern("d MMM");
            var format2 = DateTimeFormatter.ofPattern("HH:mm");
            var string1 = time.format(format1);
            var string2 = time.format(format2);

            builder.append(string1);
            builder.append(" at ");
            builder.append(string2);
        }
//        Everything older than one year
        else {
            var format1 = DateTimeFormatter.ofPattern("d MMM uuuu");
            var format2 = DateTimeFormatter.ofPattern("HH:mm");
            var string1 = time.format(format1);
            var string2 = time.format(format2);

            builder.append(string1);
            builder.append(" at ");
            builder.append(string2);
        }

        return builder.toString();
    }

    private String getStringOfDateLastRead(long timeThen) {
        var builder = new StringBuilder();
        var timeNow = Instant.now().getEpochSecond();
        var time = LocalDateTime.ofEpochSecond(timeNow - timeThen, 0, ZoneOffset.ofHours(1));

//        Message within one min
        if (timeNow - timeThen < 60) {
            var format = DateTimeFormatter.ofPattern("S");
            var string = time.format(format);

            builder.append(string);
            builder.append(" seconds");
        }
//        Message within one hour
        else if (timeNow - timeThen < 3_600) {
            var format = DateTimeFormatter.ofPattern("m");
            var string = time.format(format);

            builder.append(string);
            builder.append(" minutes");
        }
//        Message within one day
        else if (timeNow - timeThen < 86_400) {
            var format = DateTimeFormatter.ofPattern("H");
            var string = time.format(format);

            builder.append(string);
            builder.append(" hours");
        }
//        Message within one week
        else if (timeNow - timeThen < 604_800) {
            var format = DateTimeFormatter.ofPattern("F");
            var string = time.format(format);

            builder.append(string);
            builder.append(" days");
        }
//        Message within one month
        else if (timeNow - timeThen < 2_592_000) {
            var format = DateTimeFormatter.ofPattern("W");
            var string = time.format(format);

            builder.append(string);
            builder.append(" weeks");
        }
//        Message within one year
        else if (timeNow - timeThen < 31_536_000) {
            var format = DateTimeFormatter.ofPattern("M");
            var string = time.format(format);

            builder.append(string);
            builder.append(" months");
        }
//        Everything older than one year
        else {
            long years = (timeNow - timeThen) / 31_536_000;

            builder.append(years);
            builder.append(" years");
        }

        return builder.toString();
    }

}
