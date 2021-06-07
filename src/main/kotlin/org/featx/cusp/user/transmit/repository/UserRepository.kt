package org.featx.cusp.user.transmit.repository

import io.vertx.core.Future
import io.vertx.mysqlclient.MySQLClient.LAST_INSERTED_ID
import io.vertx.sqlclient.Pool
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.SqlClient
import io.vertx.sqlclient.Tuple
import org.featx.cusp.user.transmit.criteria.UserCriteria
import javax.inject.Inject
import javax.inject.Singleton

import org.featx.cusp.user.transmit.entity.UserEntity

@Singleton
class UserRepository : UserRepo {

  @Inject
  private lateinit var sqlClient: Pool

  override val connectionPool: Pool
    get() = sqlClient

  override fun create(user: UserEntity, txConnect: SqlClient?): Future<UserEntity> {
    val conn = txConnect?:sqlClient
    return conn.preparedQuery("insert into t_user(" +
            "`code`, `name`, `type`, `username`, `password`, `enable`, `avatar`,`email`, `phone`, " +
            "`email_verified`, `phone_verified`) values(?,?,?,?,?,?,?,?,?,?,?)")
      .execute(
        Tuple.of(user.code, user.name, user.type, user.username, user.password, user.enable, user.avatar,
        user.email, user.phone, user.emailVerified, user.phoneVerified))
      .map {
        user.id = it.property(LAST_INSERTED_ID)
        user
      }
  }

  private fun toUserEntity(row: Row): UserEntity {
    return UserEntity(row.getLong("id"),
      row.getString("code"), row.getString("name"), row.getInteger("type"),
      row.getString("username"), row.getString("password"), row.getBoolean("enable"),
      row.getString("avatar"), row.getString("email"), row.getString("phone"),
      row.getBoolean("email_verified"), row.getBoolean("phone_verified")
    )
  }

  override fun update(user: UserEntity, txConnect: SqlClient?): Future<Boolean> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun delete(id: Long, txConnect: SqlClient?): Future<Boolean> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun retrieveOne(id: Long, txConnect: SqlClient?): Future<UserEntity> {
    val conn = txConnect?:sqlClient
    return conn.preparedQuery("select * from t_user where id = ? limit 1")
      .execute(Tuple.of(id))
      .map {
        toUserEntity(it.first())
      }
  }

  override fun count(criteria: UserCriteria, txConnect: SqlClient?): Future<Long> {
    TODO("Not yet implemented")
  }

  override fun page(criteria: UserCriteria, txConnect: SqlClient?): Future<UserEntity> {
    TODO("Not yet implemented")
  }
}
