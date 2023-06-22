package com.sverdiyev.tracker;

import com.sverdiyev.tracker.routers.BasicRestApi;
import com.sverdiyev.tracker.routers.StockRestApi;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

@SuppressWarnings("java:S106")
public class MainVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    var vertx = Vertx.vertx();

    vertx.deployVerticle(new MainVerticle(), result -> {
      if (result.failed()) {
        System.out.println("Failed to deploy");
      } else {
        System.out.println("Main Verticle deployed successfully");
      }
    });
  }

  private static void handleFailure(RoutingContext errCtx) {

    if (errCtx.response().ended()) {
      //ignore - user ended the request
      return;
    }

    System.out.printf("Route Error: %s", errCtx.failure());

    errCtx.response().setStatusCode(500).end(new JsonObject().put("message", "server error").toBuffer());
  }

  @Override
  public void start(Promise<Void> startPromise) {
    var server = vertx.createHttpServer();

    Router restApi = Router.router(vertx);

    restApi.route().failureHandler(MainVerticle::handleFailure);

    BasicRestApi.attach(restApi);
    StockRestApi.attach(restApi);

    server.requestHandler(restApi).listen(8888).onSuccess(s -> {
      System.out.println("Server started");

      //signals to vertex that start of this vertical is complete, and it can move on to further initialization of verticles
      startPromise.complete();
    });

  }
}
