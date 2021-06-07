package org.featx.cusp.user

import com.google.inject.Injector
import io.vertx.core.Promise
import io.vertx.core.spi.VerticleFactory
import java.util.concurrent.Callable
import io.vertx.core.Verticle

class GuiceVerticleFactory(private val injector: Injector) : VerticleFactory {
    override fun prefix(): String {
        return "org.featx.cusp.user"
    }

    override fun createVerticle(verticleName: String, classLoader: ClassLoader, promise: Promise<Callable<Verticle>>) {
        val verticle = VerticleFactory.removePrefix(verticleName)
        promise.complete { injector.getInstance(classLoader.loadClass(verticle)) as Verticle }
    }
}