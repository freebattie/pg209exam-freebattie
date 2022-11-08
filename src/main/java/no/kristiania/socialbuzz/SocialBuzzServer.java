package no.kristiania.socialbuzz;

import jakarta.servlet.DispatcherType;
import no.kristiania.socialbuzz.db.DataSourceFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EnumSet;

public class SocialBuzzServer {

    private final Server server;
    private final static Logger logger = LoggerFactory.getLogger(SocialBuzzServer.class);

    public SocialBuzzServer(int port, DataSource dataSource) throws IOException {
        this.server = new Server(port);
        server.setHandler(createWebApp(dataSource));
    }

    private static WebAppContext createWebApp(DataSource dataSource) throws IOException {
        var webAppContext = new WebAppContext();
        webAppContext.setContextPath("/");
        var resources = Resource.newClassPathResource("/webapp");

        useFolderIfExist(webAppContext, resources);
//        resource that is read from .../target/classes/...

        //webAppContext.setInitParameter(DefaultServlet.CONTEXT_INIT + "useFileMappedBuffer", "false");

        var config = new SocialBuzzEndpointConfig(dataSource);
        webAppContext.addServlet(new ServletHolder(new ServletContainer(config)), "/api/*");
        webAppContext.addFilter(new FilterHolder(new DataSourceFilter(config)),"/api/*", EnumSet.of(DispatcherType.REQUEST));

        return webAppContext;
    }

    private static void  useFolderIfExist(WebAppContext webAppContext, Resource resources) throws IOException {
        if (resources.getFile() == null) {
            webAppContext.setBaseResource(resources);
            return;
        }

        var sourceDirectory = new File(resources.getFile().getAbsoluteFile().toString()
                .replace('\\', '/')
                .replace("target/classes", "src/main/resources"));

        if (sourceDirectory.isDirectory()) {
            webAppContext.setBaseResource(Resource.newResource(sourceDirectory));
            webAppContext.setInitParameter(DefaultServlet.CONTEXT_INIT + "useFileMappedBuffer", "false");

        } else {
            webAppContext.setBaseResource(resources);
        }

    }


    public void start() throws Exception {
        server.start();
        logger.warn("Server is starting on {}", getURL());
    }

    public URL getURL() throws MalformedURLException {
        return server.getURI().toURL();
    }

}
