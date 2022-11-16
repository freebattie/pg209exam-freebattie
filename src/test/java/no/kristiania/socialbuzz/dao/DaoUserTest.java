package no.kristiania.socialbuzz.dao;

import no.kristiania.socialbuzz.db.InMemoryDataSource;
import no.kristiania.socialbuzz.dto.Email;
import no.kristiania.socialbuzz.dto.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class DaoUserTest {

    private Connection connection;
    private DaoUser dao;

    @BeforeEach
    public void h2DbSetup() throws SQLException {
        this.connection = InMemoryDataSource.createTestDataSource().getConnection();
        connection.setAutoCommit(false);
        this.dao = new DaoUser(connection);
    }

    @AfterEach
    public void rollbackTest() throws SQLException {
        connection.rollback();
    }

    @Test
    public void getAllUserLoginTest() throws SQLException {
        var result = dao.getAllUserLogin();

        assertThat(result.get(0).getUsername())
                .as("Check if id_user and username match")
                .isEqualTo("Karigirl");

        assertThat(result.get(1).getUsername())
                .as("Check if id_user and username match")
                .isEqualTo("SecretMan");
    }

    @Test
    public void getUserById() throws SQLException {
        dao = new DaoUser(InMemoryDataSource.createTestDataSource().getConnection());
        var user = dao.getUserById(2);

        // Check that user is the expected user
        assertThat(user.getUsername())
                .as("Check that user is the expected user")
                .isEqualTo("SecretMan");
    }

    @Test
    public void getNotValidUserById() throws SQLException {
        dao = new DaoUser(InMemoryDataSource.createTestDataSource().getConnection());
        var user = dao.getUserById(99);

        // Check that user is the expected user
        assertThat(user)
                .as("return null if user dose not exist")
                .isEqualTo(null);
    }

    @Test
    public void getEditedUserBack() throws SQLException {
        dao = new DaoUser(InMemoryDataSource.createTestDataSource().getConnection());
        var original = dao.getUserById(4);
        var editUser = dao.getUserById(4);

        editUser.setUsername("NotSecretman");
        Email mail = editUser.getEmails().get(0);
        mail.setEmail("test@test.no");

        dao.editUser(editUser);
        var updatedUser = dao.getUserById(4);

        //CHECK BEFORE EDIT
        assertThat(original.getUsername())
                .as("is not same as original")
                .isNotEqualTo(editUser.getUsername());

        //CHECKING THAT EDITED USER IS SAME AS ORIGINAL
        assertThat(updatedUser.getId_user())
                .as("check that id is same as original")
                .isEqualTo(original.getId_user());


        var emails = updatedUser.getEmails();

        assertThat(emails.get(0).getEmail().toLowerCase())
                .as("Has Original Email")
                .isEqualTo("test@test.no".toLowerCase());


        //CHECK AFTER EDIT
        //CHECK THAT UPDATED USER FROM DB IS SAME AS THE USER WE EDITED
        assertThat(updatedUser.getUsername())
                .as("check that edited user is same as user in db")
                .isEqualTo(editUser.getUsername());

        emails = updatedUser.getEmails();

        assertThat(emails.get(0).getEmail().toLowerCase())
                .as("First email has been updated")
                .isEqualTo("test@test.no".toLowerCase());
    }

    @Test
    public void createNewUserTest() throws SQLException {
        var user = new User();
        user.setUsername("Testuser");
        user.setName("Testerson");
        user.setTlf("74857485");

        var email1 = new Email();
        var email2 = new Email();
        email1.setEmail("test@test.no");
        email2.setEmail("test@gmail.com");
        user.setEmails(email1);
        user.setEmails(email2);

        dao.createNewUser(user);
        var testUser = dao.getUserById(5);

        assertThat(testUser)
                .as("Is user and testUser equals")
                .hasNoNullFieldsOrProperties()
                .isNotSameAs(user);

        assertEquals(user.getName(), testUser.getName());
        assertEquals(user.getUsername(), testUser.getUsername());
        assertEquals(user.getTlf(), testUser.getTlf());
        assertEquals(user.getEmails().size(), testUser.getEmails().size());
    }

}
