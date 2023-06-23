package com.sverdiyev.tracker.routers;

import com.sverdiyev.tracker.models.Quote;
import com.sverdiyev.tracker.models.Stock;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


public class QuotesRestApi {


  private static final Map<String, Quote> cachedQuotes = new HashMap<>();

  private QuotesRestApi() {
  }

  public static void attach(Router parent) {
    parent.get("/quotes/:stockName").handler(ctx -> {

      var stockName = ctx.pathParam("stockName");
      var cachedQuotes = getCachedQuotes();

      var cachedQuote = cachedQuotes.get(stockName);
      if (cachedQuote != null) {
        var arr = new JsonArray();
        arr.add(cachedQuotes.get(stockName));
        ctx.response().end(arr.toBuffer());
      } else {

        var res = (new JsonObject());

        res.put("message", "No Quote was found");
        ctx.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end(res.toBuffer());
      }
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


  private static Map<String, Quote> getCachedQuotes() {

    List<String> symbols = List.of("AAPL", "SP500", "GGL", "NTFLX");

    for (String symbol : symbols) {
      cachedQuotes.put(symbol, randomQuote(symbol));
    }

    return cachedQuotes;
  }


  private static BigDecimal randomValue() {
    return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
  }
}
