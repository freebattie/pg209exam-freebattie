package no.kristiania.socialbuzz.db;

import no.kristiania.socialbuzz.dao.DaoUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;


public class DaoUserTest {

    private DaoUser dao;

    @BeforeEach
    public void h2DbSetup() throws SQLException {
        var con = InMemoryDataSource.createTestDataSource();
         this.dao = new DaoUser(con.getConnection());
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
        var user = dao.getUser(2);

        // Check that user is the expected user
        assertThat(user.getUsername())
                .as("Check that user is ")
                .isEqualTo("SecretMan");
    }

    @Test
    public void getEditedUserBack() throws SQLException {
        dao = new DaoUser(InMemoryDataSource.createTestDataSource().getConnection());
        var original = dao.getUser(1);
        var editUser = dao.getUser(1);
        editUser.setUsername("NotSecretman");
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
        var emails = original.getEmails();
        //TODO: add a email and check we get new emails
        assertThat(emails.get(0).toLowerCase())
                .as("Has Email One")
                .isEqualTo("Karinordman@online.no".toLowerCase());

        assertThat(emails.get(1).toLowerCase())
                .as("Has Email Two")
                .isEqualTo("KariNordman@outlook.no".toLowerCase());
    }

    //TODO: EDIT EMAIL and check if updated

}
