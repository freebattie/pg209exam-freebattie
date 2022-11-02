package no.kristiania.socialbuzz;

import jakarta.servlet.*;

import java.io.IOException;
import java.sql.SQLException;

public class DataSourceFilter implements Filter {

    private final WebshopEndpointConfig config;
    public DataSourceFilter(WebshopEndpointConfig config) {
        this.config = config;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)  throws IOException, ServletException {

        try {
//            Get threadsafe connection from HikariCP.
            var connection = config.createConnectionForRequest();
//            Turn of auto commit, so we can manage connection within filter.
            connection.setAutoCommit(false);
//            Sends the connection of do relevant DAO
            filterChain.doFilter(servletRequest, servletResponse);
            connection.commit();
            connection.close();
//            Remove closed connection at HikariCP
            config.cleanRequestConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
