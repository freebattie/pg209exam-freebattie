package no.kristiania.socialbuzz.dao;

import jakarta.inject.Inject;
import no.kristiania.socialbuzz.dto.Email;
import no.kristiania.socialbuzz.dto.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DaoUser {

    private final Connection connection;

    @Inject
    public DaoUser(Connection connection) {
        this.connection = connection;
    }

    //    Used for getting the necessary info at login
    public List<User> getAllUserLogin() throws SQLException {

        var sql = """
                SELECT id_user, username
                FROM users;
                """;

        try (var statement = connection.prepareStatement(sql)) {
            List<User> users = new ArrayList<>();
            var result = statement.executeQuery();


            while (result.next()) {
                var tmpUser = new User();
                tmpUser.setId_user(result.getLong(1));
                tmpUser.setUsername(result.getString(2));
                users.add(tmpUser);
            }

            return users;
        }

    }

    public void deleteEmailByEmailId(long id) throws SQLException {
        var sql = """
                DELETE FROM emails WHERE id_email = ?;
                """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();


        }
    }

    public long getLastEmailId(long id) throws SQLException {

        var sql = """
                SELECT id_email
                FROM emails
                WHERE id_user = ?
                ORDER BY id_email desc
                OFFSET 0 ROW
                FETCH NEXT 1 ROW ONLY;
                """;

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            var result = statement.executeQuery();

            result.next();


            return result.getLong(1);
        }

    }

    public void addEmailByUserId(long id) throws SQLException {
        System.out.println(id);
        var sql = """
                    INSERT INTO emails (id_user, email)
                    VALUES (?,?);
                """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.setString(2, "");

            statement.executeUpdate();

        }
    }

    public void editUser(User user) throws SQLException {
        System.out.println(user.getId_user());
        var sql = """
                UPDATE users
                           SET  username = ?,
                                name = ?,
                                tlf = ?
                           WHERE id_user = ?;
                """;

        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getName());
            statement.setString(3, user.getTlf());
            statement.setLong(4, user.getId_user());
            statement.executeUpdate();

        }
        sql = """
                    UPDATE emails
                        Set email =?
                        Where id_email = ?
                """;
        for (var mail : user.getEmails()) {


            try (var statement = connection.prepareStatement(sql)) {
                statement.setString(1, mail.getEmail());
                statement.setLong(2, mail.getId());
                statement.executeUpdate();

            }
        }
    }

    // get also all emails connected to user
    public User getUserById(long id) throws SQLException {
        User user;

        var sql = """
                    SELECT *
                    FROM users WHERE id_user = ?
                """;

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (var resUser = statement.executeQuery()) {
                if (resUser.next()) {
                    user = fillUser(resUser);
                } else {
                    return null;
                }
            }
        }

        sql = """
                    SELECT id_email, email
                    FROM emails WHERE id_user = ?
                """;

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (var resUser = statement.executeQuery()) {

                while (resUser.next()) {
                    Email mail = new Email();
                    mail.setId(resUser.getLong(1));
                    mail.setEmail(resUser.getString(2));
                    user.setEmails(mail);
                }
            }
        }

        return user;
    }

    private User fillUser(ResultSet resUser) throws SQLException {
        var user = new User();
        user.setId_user(resUser.getLong(1));
        user.setUsername(resUser.getString(2));
        user.setName(resUser.getString(3));
        user.setTlf(resUser.getString(4));

        return user;
    }

    public void createNewUser(User user) throws SQLException {
        long userId;

        var sqlNewUser = """
                INSERT INTO users (username, name, tlf)
                VALUES (?, ?, ?);
                """;

        try (var statement = connection.prepareStatement(sqlNewUser, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getName());
            statement.setString(3, user.getTlf());
            statement.executeUpdate();

            var keys = statement.getGeneratedKeys();
            keys.next();
            userId = keys.getLong(1);
        }

        var sqlNewEmail = """
                INSERT INTO emails (id_user, email)
                VALUES (?, ?);
                """;

        try (var statement = connection.prepareStatement(sqlNewEmail)) {
            for (var email : user.getEmails()) {
                statement.setLong(1, userId);
                statement.setString(2, email.getEmail());
                statement.executeUpdate();
            }
        }
    }

}
