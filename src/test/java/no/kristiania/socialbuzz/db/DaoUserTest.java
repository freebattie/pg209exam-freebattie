package no.kristiania.socialbuzz.db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class DaoUserTest {

    Connection connection;

    @BeforeEach
    public void h2DbSetup() throws SQLException {
        var stuff = InMemoryDataSource.createTestDataSource();
        connection = stuff.getConnection();
        populateUsers();
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


        var result = dao.getAllUserLogin();
        assertThat(result.get(0).getUsername())
                .as("Check if id_user and username match")
                .isEqualTo("Errons1");

        assertThat(result.get(1).getUsername())
                .as("Check if id_user and username match")
                .isEqualTo("Freebattie");
    }
    @Test
    public void getUserById() throws SQLException {
        var dao = new DaoUser(connection);

        var user = dao.getUser(1);

        // Check that user is the expected user
        assertThat(user.getUsername())
                .as("Check that user is ")
                .isEqualTo("Errons1");
    }
    @Test
    public void getEditedUserBack() throws SQLException {
        var dao = new DaoUser(connection);

        var original = dao.getUser(1);
        var editUser = dao.getUser(1);
        editUser.setUsername("NotErrons1");
        dao.EditUser(editUser);
        var updatedUser = dao.getUser(1);


        assertThat(updatedUser.getId_user())
                .as("check that id is same as original")
                .isEqualTo(original.getId_user());

        assertThat(updatedUser.getUsername())
                .as("check that editeted user is same as user in db")
                .isEqualTo(editUser.getUsername());
        assertThat(original.getUsername())
                .as("is not same as original")
                .isNotEqualTo(editUser.getUsername());
    }

    //TODO: EDIT EMAIL and check if updated

}
