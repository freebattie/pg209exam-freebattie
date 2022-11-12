package no.kristiania.socialbuzz.db;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Database {

    public static HikariDataSource getDataSource() throws IOException {
        var properties = new Properties();
        var dataSource = new HikariDataSource();
        dataSource.setAutoCommit(false);
        FileReader reader;
        dataSource.setJdbcUrl(System.getenv("DB_URL"));
        dataSource.setUsername(System.getenv("DB_USER"));
        dataSource.setPassword(System.getenv("DB_PASSWORD"));
        if (new File("application.properties").exists()){



        }
        else{

        }
       //

        var flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();
        return dataSource;
    }
}
