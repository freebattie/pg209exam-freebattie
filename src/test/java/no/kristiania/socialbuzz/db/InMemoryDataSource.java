package no.kristiania.socialbuzz.db;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;

public class InMemoryDataSource {

    public static DataSource createTestDataSource() {
        var dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:testDatabase;DB_CLOSE_DELAY=-1;MODE=MSSQLServer");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("db/migrationTest")
                .load();
        flyway.migrate();
        return dataSource;
    }

}
