package no.kristiania.socialbuzz.db;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Database {

    public static HikariDataSource getDataSource() throws IOException {
        var dataSource = new HikariDataSource();
        dataSource.setAutoCommit(true);
        getDbConnectionInfo(dataSource, "application.properties");
        var flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();
        return dataSource;
    }

    public static boolean getDbConnectionInfo(HikariDataSource dataSource, String file) throws IOException {
        dataSource.setJdbcUrl(System.getenv("DB_URL"));
        dataSource.setUsername(System.getenv("DB_USER"));
        dataSource.setPassword(System.getenv("DB_PASSWORD"));


        var properties = new Properties();
        try (var reader = new FileReader(file)) {
            properties.load(reader);
            dataSource.setJdbcUrl(properties.getProperty("jdbc.url"));
            dataSource.setUsername(properties.getProperty("jdbc.username"));
            dataSource.setPassword(properties.getProperty("jdbc.password"));


        } catch (FileNotFoundException ignored) {
            System.out.println("If running localy you might be missing propeerties file");
            return false;
        }
        return true;

    }
}
