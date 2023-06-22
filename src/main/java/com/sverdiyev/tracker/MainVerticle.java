package com.sverdiyev.tracker;

import com.sverdiyev.tracker.routers.BasicRestApi;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    var vertx = Vertx.vertx();

    vertx.deployVerticle(new MainVerticle(), result -> {
      if (result.failed()) {
        System.out.println("Failed to deploy");
      }
    });
  }

  @Override
  public void start(Promise<Void> startPromise) {
    var server = vertx.createHttpServer();

    Router restApi = Router.router(vertx);

    BasicRestApi.attach(restApi);

    server.requestHandler(restApi).listen(8888);
  }
}
