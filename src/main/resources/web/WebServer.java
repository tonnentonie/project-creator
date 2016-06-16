package $package$;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;

@Command(name="web",description="Start a web server")
public class WebServer implements ApplicationCommand {
    private Logger logger = LoggerFactory.getLogger(WebServer.class);
    
    @Override
    public void run(String[] args) throws Exception {
        logger.info("Starting up web server");
        
        Vertx vertx = Vertx.vertx();
        HttpServerOptions options = new HttpServerOptions();
        HttpServer server = vertx.createHttpServer(options);
        
        Router router = Router.router(vertx);
        
        router.route().handler(CookieHandler.create());
        router.route().handler(BodyHandler.create());
        router.route().handler(context -> {
            context.response()
                   .putHeader("content-type", "application/json")
                   .end("{\"hello\": \"world\"}");
        });
        
        server.requestHandler(router::accept).listen(7788);
    }
}
