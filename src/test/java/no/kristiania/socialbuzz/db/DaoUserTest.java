package no.kristiania.socialbuzz.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class DaoUserTest {

    Connection connection;

    @BeforeEach
    public void h2DbSetup() throws SQLException {
        connection = InMemoryDataSource.createTestDataSource().getConnection();
    }

    private void populateUsers() throws SQLException {
        var statement = connection.prepareStatement("""
            INSERT INTO users (username, name, tlf)
            VALUES ('Errons1', 'Snorre', '11223344'),
                   ('Freebattie', 'Bjarte', '44332211'),
                   ('xXxNerdChruserxXx', 'Nils', '13371337');
        """);
        statement.executeUpdate();
    }

    @Test
    public void getAllUserLoginTest() throws SQLException {
        var dao = new DaoUser(connection);
        populateUsers();

        var result = dao.getAllUserLogin();
        assertThat(result.get(0).getUsername())
                .as("Check if id_user and username match")
                .isEqualTo("Errons1");

        assertThat(result.get(1).getUsername())
                .as("Check if id_user and username match")
                .isEqualTo("Freebattie");
    }

}
