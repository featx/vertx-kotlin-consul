package com.juext.artisan.ucs

import com.juext.artisan.ucs.enums.EventBusChannels
import com.juext.artisan.ucs.handler.UserHandler
import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.kotlin.config.configRetrieverOptionsOf
import io.vertx.kotlin.config.configStoreOptionsOf
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import javax.inject.Inject
import kotlin.system.exitProcess

class MainVerticle : AbstractVerticle() {

  @Inject
  private var userHandler: UserHandler? = null

  override fun start(startFuture: Future<Void>) {
    val router = Router.router(vertx)
    router.route().path("/user/*").handler(userHandler)
//    router.route().path()
    vertx.createHttpServer().requestHandler(router).listen(8888) { http ->
      if (http.succeeded()) {
        startFuture.complete()
        println("HTTP server started on port 8888")
      } else {
        startFuture.fail(http.cause())
      }
    }
  }
}

fun main(args: Array<String>) {
  val vertx = Vertx.vertx()
  val configRetrieverOptions = getConfigRetrieverOptions()
  val configRetriever = ConfigRetriever.create(vertx, configRetrieverOptions)

  // getConfig is called for initial loading
  configRetriever.getConfig { ar ->
    if (ar.failed()) {
      println("Load config error" + ar.cause().message)
      exitProcess(-1)
    }
    val config = ar.result()
    config.put("guice_binder", AppContext::class.java.name)
    val instances = Runtime.getRuntime().availableProcessors()
    val deploymentOptions = DeploymentOptions().setInstances(instances).setConfig(config)
    vertx.deployVerticle("java-guice:" + MainVerticle::class.java.name, deploymentOptions)
  }
  // listen is called each time configuration changes
  configRetriever.listen { change ->
    val updatedConfiguration = change.getNewConfiguration()
    vertx.eventBus().publish(EventBusChannels.CONFIGURATION_CHANGED.name, updatedConfiguration)
  }
}

fun getConfigRetrieverOptions(): ConfigRetrieverOptions {
  val fileStore = configStoreOptionsOf(
    type = "file",
    format = "properties",
    config = json {
      obj("path" to "application.properties")
    })

  val sysPropsStore = configStoreOptionsOf(
    type = "sys",
    optional = true
  )

  val httpStore = configStoreOptionsOf(
    type = "http",
    format = "properties",
    optional = true,
    config = json {
      obj(
        "host" to "localhost",
        "port" to 8510,
        "path" to "/v1/kv/config/ubs,dev/data"
      )
    })
  return configRetrieverOptionsOf(
    includeDefaultStores = true,
    //Tested, priority raised as order.
    stores = listOf(sysPropsStore, fileStore, httpStore),
    scanPeriod = 5000
  )
}
