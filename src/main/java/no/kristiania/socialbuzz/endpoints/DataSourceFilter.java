package no.kristiania.socialbuzz.endpoints;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import no.kristiania.socialbuzz.SocialBuzzEndpointConfig;
import no.kristiania.socialbuzz.SocialBuzzServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

public class DataSourceFilter implements Filter {

    private final SocialBuzzEndpointConfig config;
    public DataSourceFilter(SocialBuzzEndpointConfig config) {
        this.config = config;
    }
    private static final Logger logger = LoggerFactory.getLogger(SocialBuzzServer.class);
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)  throws IOException, ServletException {


        try {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            HttpServletResponse res = (HttpServletResponse) servletResponse;

            logger.info("Request  Method: {} \"{}\"", req.getMethod(), req.getRequestURI());
            logger.info("Response Code from Server: {}", res.getStatus());
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
