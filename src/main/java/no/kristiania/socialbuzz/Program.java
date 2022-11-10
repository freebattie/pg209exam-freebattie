package no.kristiania.socialbuzz;

import no.kristiania.socialbuzz.db.Database;

import java.util.Optional;

public class Program {

    public static void main(String[] args) throws Exception {
        var dataSource = Database.getDataSource();
        int port = Optional.ofNullable(System.getenv("HTTP_PLATFORM_PORT"))
                .map(Integer::parseInt)
                .orElse(8080);
        new SocialBuzzServer(port, dataSource).start();
    }

}
