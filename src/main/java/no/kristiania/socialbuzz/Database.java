package no.kristiania.socialbuzz;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Database {

    public static HikariDataSource getDataSource() throws IOException {
        var properties = new Properties();
        var dataSource = new HikariDataSource();

//        if we upload with a .properties use that setting. otherwise use azure environment variables
        if (Files.exists(Path.of("application.properties"))){
            FileReader reader = new FileReader("application.properties");
                try (reader) {
                properties.load(reader);

                dataSource.setJdbcUrl(properties.getProperty("jdbc.url"));
                dataSource.setUsername(properties.getProperty("jdbc.username"));
                dataSource.setPassword(properties.getProperty("jdbc.password"));
            }
        }
        else{
            dataSource.setJdbcUrl(System.getenv("DB_URL"));
            dataSource.setUsername(System.getenv("DB_USER"));
            dataSource.setPassword(System.getenv("DB_PASSWORD"));
        }

        var flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();
        return dataSource;
    }
}
