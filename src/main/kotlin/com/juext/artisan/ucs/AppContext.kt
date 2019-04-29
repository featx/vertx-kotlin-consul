package com.juext.artisan.ucs;

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.juext.artisan.ucs.service.UserService
import com.juext.artisan.ucs.service.UserServiceImpl
import com.juext.artisan.ucs.transmit.repository.UserRepo
import com.juext.artisan.ucs.transmit.repository.UserRepository
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.jdbc.JDBCClient
import io.vertx.ext.sql.SQLClient
import javax.inject.Singleton


class AppContext : AbstractModule() {

  override fun configure() {
    bind(UserService::class.java).to(UserServiceImpl::class.java).`in`(Singleton::class.java)
    bind(UserRepo::class.java).to(UserRepository::class.java).`in`(Singleton::class.java)
  }

  @Provides
  @Singleton
  fun provideJDBCClient(vertx: Vertx): SQLClient {
    return JDBCClient.createShared(
      vertx, JsonObject()
        .put("provider_class", "io.vertx.ext.jdbc.spi.impl.HikariCPDataSourceProvider")
        .put("driverClassName", "com.mysql.cj.jdbc.Driver")
        .put("jdbcUrl", "jdbc:mysql://localhost:3306/ubs")
        .put("username", "root")
        .put("password", "root")
        .put("maximumPoolSize", 20)
        .put("minimumIdle", 10)
        .put("cachePrepStmts", true)
        .put("prepStmtCacheSize", 250)
        .put("prepStmtCacheSqlLimit", 2048)
    )
  }
}
