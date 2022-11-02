package no.kristiania.socialbuzz.db;

import jakarta.inject.Inject;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class DaoUser {

    private final Connection connection;

    @Inject
    public DaoUser(Connection connection) {
        this.connection = connection;
    }

//    Used for getting the necessary info at login
    public Map<Integer, String> getAllUserLogin() {

        return null;
    }

    public List<User> getAllUsers() {

        return null;
    }

}
