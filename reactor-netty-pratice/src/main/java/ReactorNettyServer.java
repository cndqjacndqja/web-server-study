import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;

public class ReactorNettyServer {

    public static void main(String[] args) {
        HttpServer.create()
            .port(8080)
            .route(routes ->
                routes.get("/hello", (req, res) ->
                    res.sendString(Mono.just("Hello, Reactor Netty!"))
                )
            )
            .bindNow()
            .onDispose()
            .block();
    }
}
