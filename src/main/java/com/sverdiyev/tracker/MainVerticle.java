package com.sverdiyev.tracker;

import com.sverdiyev.tracker.verticles.RestApiVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MainVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    var vertx = Vertx.vertx();

    vertx.deployVerticle(MainVerticle.class.getName(), new DeploymentOptions().setInstances(1), result -> {

//      log.info("Main Verticle vertx id: {}", Vertx.currentContext().deploymentID());

      if (result.failed()) {
        log.info("Failed to deploy");
      } else {
        log.info("Main Verticle deployed successfully");
      }
    });
  }

  @Override
  public void start(Promise<Void> startPromise) {

    vertx.deployVerticle(RestApiVerticle.class.getName(), new DeploymentOptions().setInstances(4))
      .onSuccess(res -> {
          log.info("successfully deployed RestApiVerticles");
          startPromise.complete();
        }
      );
  }
}
