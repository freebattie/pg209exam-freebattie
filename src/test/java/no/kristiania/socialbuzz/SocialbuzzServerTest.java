package no.kristiania.socialbuzz;

import no.kristiania.socialbuzz.db.InMemoryDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;


class SocialbuzzServerTest {

    private SocialbuzzServer server;


    @BeforeEach
    public void setupServer() throws Exception {
        this.server = new SocialbuzzServer(0, InMemoryDataSource.createTestDataSource());
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
                .contains("<title>SocialBuzz</title>");
    }


    private HttpURLConnection openConnection(String spec) throws IOException {
        return (HttpURLConnection) new URL(server.getURL(), spec).openConnection();
    }

}