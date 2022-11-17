package no.kristiania.socialbuzz;

import no.kristiania.socialbuzz.db.Database;

import java.util.Optional;

public class Program {

    public static void main(String[] args) throws Exception {
        new SocialBuzzServer(getPortToUse(), Database.getDataSource()).start();
    }

    public static Integer getPortToUse() {
        return Optional.ofNullable(System.getenv("HTTP_PLATFORM_PORT"))
                .map(Integer::parseInt)
                .orElse(8080);
    }

}
