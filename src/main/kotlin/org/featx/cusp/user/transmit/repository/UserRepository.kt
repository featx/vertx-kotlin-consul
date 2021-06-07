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
//  @Inject
//  private lateinit var connectionPool: Pool
  //  private <T>fun aop(): Single<T> {
//    Single.create {  }
//  }
  //  it.rxGetTransactionIsolation()
//  it.rxExecute("insert into t_ucs_user (code, name, type, email, phone, email_verified, phone_verified) values () ").
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

//    return Single.create {
//      val emitter = it
//      sqlClient?.getConnection {
//        if (it.failed()) {
//          emitter.onError(it.cause())
//          return@getConnection
//        }
//        val connection = it.result()
//        connection.setAutoCommit(false) {
//          if (it.failed()) {
//            emitter.onError(it.cause())
//            return@setAutoCommit
//          }
//          connection.updateWithParams(
//            "Insert into t_ubs_user (code, name, type, email, phone) values (?,?,?,?,?)",
//            JsonArray().add(user.code).add(user.name).add(user.type).add(user.email).add(user.phone)
//          ) {
//            if (it.failed()) {
//              connection.rollback {
//                emitter.onError(it.cause())
//              }
//              return@updateWithParams
//            }
//            connection.query("select last_insert_id() as id") {
//              val rs = it.result()
//              if (it.failed()) {
//                connection.rollback {
//                  emitter.onError(it.cause())
//                }
//                return@query
//              }
//              connection.commit {
//                user.id = rs.results[0].getLong(0)
//                emitter.onSuccess(user)
//              }
//            }
//          }
//        }
//      }
//    }
//  }

//    return Single.
//    connection.rxSetAutoCommit(false).blockingAwait()
//    return connection.rxUpdateWithParams(
//      "Insert into t_ubs_user (code, name, type, email, phone) values (?,?,?,?,?)",
//      JsonArray().add(user.code).add(user.name).add(user.type).add(user.email).add(user.phone)
//    ).flatMap {
//      connection.rxQuery("select last_insert_id() as id")
//    }.map {
//      val r = it.rows as BigInteger
//      user.id = r.toLong()
//      user
//    }.onErrorReturn {
//      LoggerFactory.getLogger(this::class.java).error("Sql execution error", it)
//      connection.rxRollback().`as` {
//        user
//      }
//    }

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
  //    return Single.create {
//      val emitter = it
//      sqlClient?.querySingleWithParams(
//        "select id, name, code, type, email, phone, email_verified, phone_verified " +
//          "from t_ubs_user where id = ? and is_deleted = 0 limit 1", JsonArray().add(id)
//      ) {
//        if (it.failed()) {
//          emitter.onError(it.cause())
//        } else {
//          val jsonArray = it.result()
//          val result = UserEntity(
//            jsonArray.getLong(0),
//            jsonArray.getString(1),
//            jsonArray.getString(2),
//            jsonArray.getInteger(3),
//            jsonArray.getString(4),
//            jsonArray.getString(5),
//            !0.equals(jsonArray.getInteger(6)),
//            !0.equals(jsonArray.getInteger(7))
//          )
//          emitter.onSuccess(result)
//        }
//      }
//    }
  }

  override fun count(criteria: UserCriteria, txConnect: SqlClient?): Future<Long> {
    TODO("Not yet implemented")
  }

  override fun page(criteria: UserCriteria, txConnect: SqlClient?): Future<UserEntity> {
    TODO("Not yet implemented")
  }

  override val connectionPool: Pool
    get() = sqlClient
}
