package no.kristiania.socialbuzz.endpoints;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import no.kristiania.socialbuzz.SocialBuzzEndpointConfig;
import no.kristiania.socialbuzz.SocialBuzzServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceFilter implements Filter {

    private final SocialBuzzEndpointConfig config;

    public DataSourceFilter(SocialBuzzEndpointConfig config) {
        this.config = config;
    }

    private static final Logger logger = LoggerFactory.getLogger(SocialBuzzServer.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        Connection connection;
        try {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            HttpServletResponse res = (HttpServletResponse) servletResponse;

            // if invalid path redirect to / and index.html
            if (!req.getRequestURI().contains("/api/") && !req.getRequestURI().contains("/assets") && !req.getRequestURI().equals("/")) {
                logger.info("Request  Method: {} \"{}\"", req.getMethod(), req.getRequestURI());
                ((HttpServletResponse) servletResponse).sendRedirect("/");
                logger.info("Response Code from Server: {}", res.getStatus());

            } else {
                //Get connection from HikariCP.
                connection = config.createConnectionForRequest();
                filterChain.doFilter(servletRequest, servletResponse);
//              Remove closed connection at HikariCP after each get/put/post/delete
                logger.info("Request  Method: {} \"{}\"", req.getMethod(), req.getRequestURI());


                connection.close();
                config.cleanRequestConnection();
                logger.info("Response Code from Server: {}", res.getStatus());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
