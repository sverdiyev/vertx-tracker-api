package com.sverdiyev.tracker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
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
    restApi.get("/test").handler(ctx -> {

      JsonObject res = new JsonObject();

      res.put("hello", "world");

      ctx.response().end(res.toBuffer());
    });

    server.requestHandler(restApi).listen(8888);

//    vertx.
//      .requestHandler(req -> req.response()
//        .putHeader("content-type", "text/plain")
//        .end("Hello from Vert.x!")).listen(8888, http -> {
//        if (http.succeeded()) {
//          startPromise.complete();
//          System.out.println("HTTP server started on port 8888");
//        } else {
//          startPromise.fail(http.cause());
//        }
//      });
  }
}
