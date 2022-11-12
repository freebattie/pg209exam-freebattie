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
        var dataSource = new HikariDataSource();
        dataSource.setAutoCommit(false);
        var properties = new Properties();
        try (var reader = new FileReader("application.properties")) {
            properties.load(reader);
        }


        dataSource.setJdbcUrl(properties.getProperty("jdbc.url"));
        dataSource.setUsername(properties.getProperty("jdbc.username"));
        dataSource.setPassword(properties.getProperty("jdbc.password"));

//        dataSource.setJdbcUrl(System.getenv("DB_URL"));
//        dataSource.setUsername(System.getenv("DB_USER"));
//        dataSource.setPassword(System.getenv("DB_PASSWORD"));

       //

        var flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();
        return dataSource;
    }
}
