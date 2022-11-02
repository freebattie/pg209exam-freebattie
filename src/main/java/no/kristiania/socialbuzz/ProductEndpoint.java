package no.kristiania.socialbuzz;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import no.kristiania.webshop.db.ProductDao;

import java.sql.SQLException;
import java.util.List;


@Path("/products")
public class ProductEndpoint {

    @Inject
    private ProductDao productDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getAllProducts() throws SQLException {
        return productDao.getAllProduct();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addProducts(Product product) throws SQLException {
        productDao.saveProduct(product);
    }

}
