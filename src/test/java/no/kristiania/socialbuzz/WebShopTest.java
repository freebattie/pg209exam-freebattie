package no.kristiania.socialbuzz;

import jakarta.json.Json;
import no.kristiania.socialbuzz.db.InMemoryDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class WebShopTest {

    private WebShop server;


    @BeforeEach
    public void setupServer() throws Exception {
        this.server = new WebShop(0, InMemoryDataSource.createTestDataSource());
        server.start();
    }

    @Test
    public void getStatusCode200Test() throws Exception {
        var connection = openConnection("/");
        assertThat(connection.getResponseCode())
                .as("check for 200")
                .isEqualTo(200);
    }

    @Test
    public void getIndexHtmlTitleTest() throws Exception {
        var connection = openConnection("/");
        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("<title>shop</title>");
    }

    @Test
    public void PostRequestAddProductTest() throws Exception {
        var postConnection = openConnection("/api/products");
        postConnection.setRequestMethod("POST");
        postConnection.setDoOutput(true);
        postConnection.setRequestProperty("Content-type","application/json");
        postConnection.getOutputStream().write(Json.createObjectBuilder()

                .add("name","testlaptop")
                .add("category","pc")
                .add("img","test")
                .add("proddesc","alaptop")
                .add("price",299)
                .add("stock",100).build()
                .toString()
                .getBytes(StandardCharsets.UTF_8)
        );

        assertThat(postConnection.getResponseCode())
                .as("Check if POST worked")
                .isEqualTo(204);

        var getConnection = openConnection("/api/products");
        assertThat(getConnection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("testlaptop");
    }


    private HttpURLConnection openConnection(String spec) throws IOException {
        return (HttpURLConnection) new URL(server.getURL(), spec).openConnection();
    }

}