package no.kristiania.socialbuzz;

import no.kristiania.socialbuzz.db.old.JdbcProductDao;
import no.kristiania.socialbuzz.db.old.ProductDao;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class WebshopEndpointConfig extends ResourceConfig {

    private final ThreadLocal<Connection> requestConnection = new ThreadLocal<>();
    private final DataSource dataSource;

    public WebshopEndpointConfig(DataSource dataSource) {
        super(ProductEndpoint.class);

        this.dataSource = dataSource;
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(JdbcProductDao.class).to(ProductDao.class);
                bindFactory(requestConnection::get)
                        .to(Connection.class)
                        .in(RequestScoped.class);
            }
        });
    }
    public Connection createConnectionForRequest() throws SQLException {
        requestConnection.set(dataSource.getConnection());
        return requestConnection.get();
    }

    public void cleanRequestConnection() {
        requestConnection.remove();
    }
}
