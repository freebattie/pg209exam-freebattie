package no.kristiania.socialbuzz.dao;

import no.kristiania.socialbuzz.db.InMemoryDataSource;
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
                .as("Check that user is the expexted user")
                .isEqualTo("SecretMan");
    }

    @Test
    public void getEditedUserBack() throws SQLException {

        //TODO: kanskje dele denne opp i flere tester siden vi tester veldig mye her


        dao = new DaoUser(InMemoryDataSource.createTestDataSource().getConnection());
        var original = dao.getUser(1);
        var editUser = dao.getUser(1);
        editUser.setUsername("NotSecretman");
        EMail mail = editUser.getEmails().get(0);
        mail.setEmail("test@test.no");
        int id = Math.toIntExact(mail.getId());
        editUser.EditMail(mail,id);
        dao.EditUser(editUser);
        var updatedUser = dao.getUser(1);






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
                .as("check that editeted user is same as user in db")
                .isEqualTo(editUser.getUsername());


        emails = updatedUser.getEmails();


        assertThat(emails.get(0).getEmail().toLowerCase())
                .as("First email has been updated")
                .isEqualTo("test@test.no".toLowerCase());





    }

    //TODO: EDIT EMAIL and check if updated

}
