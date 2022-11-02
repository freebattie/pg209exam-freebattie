package no.kristiania.socialbuzz.db;

import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaoUser {

    private final Connection connection;

    @Inject
    public DaoUser(Connection connection) {
        this.connection = connection;
    }

//    Used for getting the necessary info at login
    public Map<Integer, String> getAllUserLogin() throws SQLException {

        var sql = """
                SELECT id_user, username
                FROM users;
                """;

        try (var statement = connection.prepareStatement(sql)) {
            Map<Integer, String> users = new HashMap<>();
            var result = statement.executeQuery();

            while (result.next()) {
                users.put(result.getInt(1), result.getString(2));
            }

            return users;
        }

    }

    public List<User> getAllUsers() {

        return null;
    }

}
