package com.sverdiyev.tracker.verticles;

import com.sverdiyev.tracker.routers.BasicRestApi;
import com.sverdiyev.tracker.routers.QuotesRestApi;
import com.sverdiyev.tracker.routers.StockRestApi;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestApiVerticle extends AbstractVerticle {


  private static void handleFailure(RoutingContext errCtx) {

    if (errCtx.response().ended()) {
      //ignore - user ended the request
      return;
    }

    log.error("Route Error: ", errCtx.failure());

    errCtx.response().setStatusCode(500).end(new JsonObject().put("message", "server error").toBuffer());
  }

  @Override
  public void start(Promise<Void> startPromise) {
    var server = vertx.createHttpServer();

    log.info("Rest API Verticle vertx id: {}", deploymentID());

    Router restApi = Router.router(vertx);

    restApi.route()
      .handler(BodyHandler.create()) //enables passing body to request objects in handlers
      .handler(ctx -> {
        log.info("Current vertx id: {}", Vertx.currentContext().deploymentID());
//        try {
//          Thread.sleep(2000);
//        } catch (InterruptedException e) {
//          throw new RuntimeException(e);
//        }
        ctx.next();
      })
      .failureHandler(RestApiVerticle::handleFailure);

    BasicRestApi.attach(restApi);
    StockRestApi.attach(restApi);
    QuotesRestApi.attach(restApi);

    server.requestHandler(restApi).listen(8888).onSuccess(s -> {
      log.info("Server started");

      //signals to vertex that start of this vertical is complete, and it can move on to further initialization of verticles
      startPromise.complete();
    });
  }

}
