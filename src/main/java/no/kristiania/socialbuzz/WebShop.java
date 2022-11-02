package no.kristiania.socialbuzz;

import jakarta.servlet.DispatcherType;
import no.kristiania.webshop.db.JdbcProductDao;
import no.kristiania.webshop.db.ProductDao;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class WebShop {

    private final Server server;
    private final static Logger logger = LoggerFactory.getLogger(WebShop.class);

    public WebShop(int port,DataSource dataSource) throws IOException {
        this.server = new Server(port);
        server.setHandler(createWebApp(dataSource));
    }

    private static WebAppContext createWebApp(DataSource dataSource) throws IOException {
        var webAppContext = new WebAppContext();
        webAppContext.setContextPath("/");
        var resources = Resource.newClassPathResource("/webapp");

        useFolderIfExist(webAppContext, resources);
//        resource that is read from .../target/classes/...

        webAppContext.setInitParameter(DefaultServlet.CONTEXT_INIT + "useFileMappedBuffer", "false");

        var config = new WebshopEndpointConfig(dataSource);
        webAppContext.addServlet(new ServletHolder(new ServletContainer(config)), "/api/*");
        //var servletHolder = webAppContext.addServlet(ServletContainer.class, "/api/*");
        //servletHolder.setInitParameter("jersey.config.server.provider.packages", "no.kristiania.webshop");
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

    public static void main(String[] args) throws Exception {
        ServerStart();
        //FillServerWhitData();
    }

    private static void ServerStart() throws Exception {
        var dataSource = Database.getDataSource();
        int port = Optional.ofNullable(System.getenv("HTTP_PLATFORM_PORT"))
                .map(Integer::parseInt)
                .orElse(8080);
        new WebShop(port, dataSource).start();
    }

    private static void FillServerWhitData() throws IOException, SQLException {
        List<Product> products = new ArrayList<>();
        products.add(new Product(
                "acer",
                "laptop",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSOqCspEYYS3XO-De0ytx4WpAvX9OeAu_3F8vugL4ZL&s",
                "This is a good laptop",
                14955,
                15
        ));
        products.add(new Product(
                "mac air",
                "laptop",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSOqCspEYYS3XO-De0ytx4WpAvX9OeAu_3F8vugL4ZL&s",
                "best there is",
                22955,
                2
        ));
        products.add(new Product(
                "legion",
                "laptop",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSOqCspEYYS3XO-De0ytx4WpAvX9OeAu_3F8vugL4ZL&s",
                "best there is",
                22955,
                2
        ));
        products.add(new Product(
                "racer naga",
                "mouse",
                "tom",
                "best there is",
                255,
                10
        ));
        products.add(new Product(
                "dell E200",
                "mouse",
                "tom",
                "best there is",
                255,
                10
        ));
        products.add(new Product(
                "RTX3090",
                "gpu",
                "http://storage-asset.msi.com/global/picture/image/feature/vga/NVIDIA/VGA-2020/image/vga-body.png",
                "best there is",
                15000,
                100
        ));
        products.add(new Product(
                "RTX4090",
                "gpu",
                "http://storage-asset.msi.com/global/picture/image/feature/vga/NVIDIA/VGA-2020/image/vga-body.png",
                "best there is",
                25500,
                1
        ));
        //Server();
        var db = Database.getDataSource();

        ProductDao dao = new JdbcProductDao(db.getConnection());
        for (var product : products) {
            dao.saveProduct(product);
        }
    }



}
