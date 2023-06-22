package com.sverdiyev.tracker.routers;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class BasicRestApi {

  private BasicRestApi() {
  }

  public static void attach(Router parent) {
    parent.get("/test").handler(ctx -> {

      JsonObject res = new JsonObject();

      res.put("hello", "world");

      ctx.response().end(res.toBuffer());
    });
  }

}
