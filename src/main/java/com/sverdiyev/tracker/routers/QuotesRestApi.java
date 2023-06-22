package com.sverdiyev.tracker.routers;

import com.sverdiyev.tracker.models.Quote;
import com.sverdiyev.tracker.models.Stock;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;


public class QuotesRestApi {

  private QuotesRestApi() {
  }

  public static void attach(Router parent) {
    parent.get("/quotes/:stockName").handler(ctx -> {

      var stockName = ctx.pathParam("stockName");

      var res = randomQuote(stockName);

      var arr = new JsonArray();
      arr.add(res);
      ctx.response().end(arr.toBuffer());
    });
  }

  private static Quote randomQuote(String stockName) {
    return Quote.builder()
      .ask(randomValue())
      .bid(randomValue())
      .lastPrice(randomValue())
      .volume(randomValue())
      .stock(Stock.builder().ticker(stockName).build())
      .build();
  }


  private static BigDecimal randomValue() {
    return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
  }
}
