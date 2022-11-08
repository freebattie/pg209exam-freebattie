package no.kristiania.socialbuzz.dao;

import jakarta.inject.Inject;

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
                tmpUser.setId_user(result.getLong(1));
                tmpUser.setUsername(result.getString(2));
                users.add(tmpUser);
            }
            statement.close();
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
        try (var statement = connection.prepareStatement(sql)){
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getName());
            statement.setString(3, user.getTlf());
            statement.setLong(4, user.getId_user());
            statement.executeUpdate();

        }



    }
    // get also all emails connected to user
    public User getUser(long id) throws SQLException {
        User user;
        List<String> emails = new ArrayList<>();

        var sql = """
                SELECT *
                FROM users WHERE id_user = ? 
            """;

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (var resUser = statement.executeQuery()) {
                if (resUser.next()) {
                    user= fillUser(resUser);
                } else {
                    return null;
                }
            }
        }
         sql = """
                SELECT email
                FROM emails WHERE id_user = ?
            """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, 1);
            try (var resUser = statement.executeQuery()) {
                while (resUser.next()) {

                    user.addEmail(resUser.getString(1));
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

}
