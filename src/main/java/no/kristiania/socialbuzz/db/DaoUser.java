package no.kristiania.socialbuzz.db;

import jakarta.inject.Inject;
import no.kristiania.socialbuzz.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                tmpUser.setId_user(result.getInt(1));
                tmpUser.setUsername(result.getString(2));
                users.add(tmpUser);
            }

            return users;
        }

    }
    public void EditUser(User user) throws SQLException {

        var sql = """
                UPDATE users
                           SET  username = ?,
                                name = ?,
                                tlf = ?
                           WHERE id_user = ?;
                """;

        try (var statement = connection.prepareStatement(sql)) {
            List<User> users = new ArrayList<>();
            var result = statement.executeQuery();

            while (result.next()) {
                var tmpUser = new User();
                tmpUser.setId_user(result.getInt(1));
                tmpUser.setUsername(result.getString(2));
                users.add(tmpUser);
            }


        }

    }
    // get also all emails connected to user
    public User getUser(long id) throws SQLException {

        var sql = """
                SELECT *
                FROM user WHERE id = ? ;
            """;

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (var resUser = statement.executeQuery()) {
                if (resUser.next()) {
                    return fillUser(resUser);
                } else {
                    return null;
                }
            }
        }

    }

    private User fillUser(ResultSet resUser) throws SQLException {
        var user = new User();
        user.setId_user(resUser.getLong(1));
        user.setUsername(resUser.getString(2));
        user.setName(resUser.getString(3));
        user.setTlf(resUser.getString(4));
        return user;
    }

}
