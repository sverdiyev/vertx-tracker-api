package com.sverdiyev.tracker.routers;

import com.sverdiyev.tracker.models.Quote;
import com.sverdiyev.tracker.models.Stock;
import com.sverdiyev.tracker.models.StockDTO;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class QuotesRestApi {

  //concurrent - to allow access from multiple threads
  private static final Map<String, Quote> cachedQuotes = new ConcurrentHashMap<>();
  private static final List<String> symbols = new ArrayList<>(List.of("AAPL", "SP500", "GGL", "NTFLX"));

  private QuotesRestApi() {
  }

  public static void attach(Router parent) {

    populateCachedQuotes();

    parent.get("/quotes/:stockName").handler(ctx -> {

      var stockName = ctx.pathParam("stockName");

      var cachedQuote = cachedQuotes.get(stockName);
      if (cachedQuote != null) {
        var arr = new JsonArray();
        arr.add(cachedQuotes.get(stockName));
        ctx.response().end(arr.toBuffer());
        log.info("Stock was found: {}", stockName);
      } else {

        var res = (new JsonObject());

        log.warn("Stock was NOT found: {}", stockName);
        res.put("message", "No Quote was found");
        ctx.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end(res.toBuffer());
      }
    });

    parent.post("/quotes").handler(ctx -> {

      StockDTO stockName = ctx.body().asPojo(StockDTO.class);

      log.info("adding stock: {}", stockName.getName());

      cachedQuotes.put(stockName.getName(), randomQuote(stockName.getName()));
      symbols.add(stockName.getName());

      ctx.response().setStatusCode(200).end();
    });

    parent.delete("/quotes/:stockName").handler(ctx -> {

      var stockName = ctx.pathParam("stockName");
      log.info("deleting stock: {}", stockName);

      cachedQuotes.remove(stockName);
      symbols.remove(stockName);

      ctx.response().setStatusCode(200).end();
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


  private static void populateCachedQuotes() {

    log.info("cached quotes map is being populated");
    for (String symbol : symbols) {
      cachedQuotes.put(symbol, randomQuote(symbol));
    }

  }


  private static BigDecimal randomValue() {
    return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
  }
}
