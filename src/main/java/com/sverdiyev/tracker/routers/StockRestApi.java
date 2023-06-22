package com.sverdiyev.tracker.routers;

import com.sverdiyev.tracker.models.Stock;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;

public class StockRestApi {

  private StockRestApi() {
  }

  public static void attach(Router parent) {
    parent.get("/stock").handler(ctx -> {

      var res = new Stock("symbol", "AAPL");
      var arr = new JsonArray();
      arr.add(res);
      ctx.response().end(arr.toBuffer());
    });
  }

}
