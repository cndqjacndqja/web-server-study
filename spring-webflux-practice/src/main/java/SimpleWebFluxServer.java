import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.netty.http.server.HttpServer;

public class SimpleWebFluxServer {

    public static void main(String[] args) {
        RouterFunction<?> route = route(GET("/"),
            request -> ServerResponse.ok().bodyValue("Hello, WebFlux with Netty!"));

        HttpHandler httpHandler = RouterFunctions.toHttpHandler(route);
        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);

        HttpServer.create()
            .host("localhost")
            .port(8080)
            .handle(adapter)
            .bindNow()
            .onDispose()
            .block();

        System.out.println("Server is running on http://localhost:8080");
    }
}
