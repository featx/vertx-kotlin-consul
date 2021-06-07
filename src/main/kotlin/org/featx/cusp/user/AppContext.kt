package org.featx.cusp.user;

import com.google.inject.AbstractModule
import com.google.inject.Provides
import io.vertx.core.Vertx
import org.featx.cusp.user.service.UserService
import org.featx.cusp.user.service.UserServiceImpl
import org.featx.cusp.user.transmit.repository.UserRepo
import org.featx.cusp.user.transmit.repository.UserRepository
import io.vertx.mysqlclient.MySQLConnectOptions
import io.vertx.mysqlclient.MySQLPool
import io.vertx.sqlclient.Pool
import io.vertx.sqlclient.PoolOptions
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


class AppContext(var vertx: Vertx) : AbstractModule() {


  override fun configure() {
    bind(UserService::class.java).to(UserServiceImpl::class.java).`in`(Singleton::class.java)
    bind(UserRepo::class.java).to(UserRepository::class.java).`in`(Singleton::class.java)
  }

  @Provides
  @Singleton
  fun provideClient(): MySQLConnectOptions {
    return MySQLConnectOptions()
      .setHost("localhost")
      .setPort(3308)
      .setDatabase("account")
      .setUser("ex7ept")
      .setPassword("ex7ept")
      .setCachePreparedStatements(true)
      .setPreparedStatementCacheMaxSize(250)
      .setPreparedStatementCacheSqlLimit(2048)
  }

  @Provides
  @Singleton
  fun providePool(sqlConnect: MySQLConnectOptions): Pool {
    return MySQLPool.pool(vertx, sqlConnect,
      PoolOptions()
        .setMaxSize(20)
        .setIdleTimeout(10).setIdleTimeoutUnit(TimeUnit.SECONDS)
        .setConnectionTimeout(10).setConnectionTimeoutUnit(TimeUnit.SECONDS))
  }
}
