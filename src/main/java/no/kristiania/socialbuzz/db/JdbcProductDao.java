package no.kristiania.socialbuzz.db;

import jakarta.inject.Inject;
import no.kristiania.socialbuzz.Product;
import no.kristiania.webshop.Product;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcProductDao implements ProductDao {
    
    private final Connection connection;

    @Inject
    public JdbcProductDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void saveProduct(Product product) throws SQLException {

        var sql = """
                INSERT INTO products (name, category, img, description, price, stock)
                VALUES (?, ?, ?, ?, ?, ?);
            """;

        try (var statementProduct = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statementProduct.setString(1, product.getName());
            statementProduct.setString(2, product.getCategory());
            statementProduct.setString(3, product.getImg());
            statementProduct.setString(4, product.getDescription());
            statementProduct.setInt(5, product.getPrice());
            statementProduct.setInt(6, product.getStock());
            statementProduct.executeUpdate();
            try (var generatedKeys = statementProduct.getGeneratedKeys()) {
                generatedKeys.next();
                product.setId(generatedKeys.getLong(1));
            }
        }
    }
    @Override
    public List<Product> getAllProduct() throws SQLException {

            var sql = """
                SELECT *
                FROM products;
            """;

            try (var statementProducts = connection.prepareStatement(sql)) {
                List<Product> products = new ArrayList<>();
                var resultProducts = statementProducts.executeQuery();

                while (resultProducts.next()){
                    products.add(FillProduct(resultProducts));
                }
                return products;
            }
    }
    @Override
    public Product getProduct(long id) throws SQLException {

        var sql = """
                SELECT *
                FROM products WHERE id = ? ;
            """;

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (var resultProduct = statement.executeQuery()) {
                if (resultProduct.next()) {
                    return FillProduct(resultProduct);
                } else {
                    return null;
                }
            }
        }

    }

    static Product FillProduct(ResultSet rs) throws SQLException {

        var tmpProduct = new Product();
        tmpProduct.setId(rs.getLong(1));
        tmpProduct.setName(rs.getString(2));
        tmpProduct.setCategory(rs.getString(3));
        tmpProduct.setImg(rs.getString(4));
        tmpProduct.setDescription(rs.getString(5));
        tmpProduct.setPrice(rs.getInt(6));
        tmpProduct.setStock(rs.getInt(7));
        return tmpProduct;
    }

}
