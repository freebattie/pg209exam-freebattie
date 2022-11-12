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
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)  throws IOException, ServletException {

        Connection connection;
        try {
            //Get connection from HikariCP.
            connection = config.createConnectionForRequest();

            HttpServletRequest req = (HttpServletRequest) servletRequest;
            HttpServletResponse res = (HttpServletResponse) servletResponse;

            logger.info("Request  Method: {} \"{}\"", req.getMethod(), req.getRequestURI());
            logger.info("Response Code from Server: {}", res.getStatus());
//          if get dont do commit
            if (req.getMethod().equals("GET")) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {


                filterChain.doFilter(servletRequest, servletResponse);
                connection.commit();
            }

//          Remove closed connection at HikariCP after each get/put/post/delete
            connection.close();
            config.cleanRequestConnection();



        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

}
