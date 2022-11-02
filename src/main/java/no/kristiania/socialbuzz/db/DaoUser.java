package no.kristiania.socialbuzz.db;

import jakarta.inject.Inject;
import no.kristiania.socialbuzz.User;

import java.sql.Connection;
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
                tmpUser.setUsername(result.getString("2"));
                users.add(tmpUser);
            }

            return users;
        }

    }

}
