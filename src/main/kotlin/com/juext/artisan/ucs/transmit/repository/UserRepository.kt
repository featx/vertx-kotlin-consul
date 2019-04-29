package com.juext.artisan.ucs.transmit.repository

import com.juext.artisan.ucs.transmit.entity.UserEntity
import io.reactivex.Single
import io.vertx.core.json.JsonArray
import io.vertx.ext.sql.SQLClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository : UserRepo {

  @Inject
  private var sqlClient: SQLClient? = null

  //  private <T>fun aop(): Single<T> {
//    Single.create {  }
//  }
  //  it.rxGetTransactionIsolation()
//  it.rxExecute("insert into t_ucs_user (code, name, type, email, phone, email_verified, phone_verified) values () ").
  override fun create(user: UserEntity): Single<UserEntity> {
    return Single.create {
      val emitter = it
      sqlClient?.getConnection {
        if (it.failed()) {
          emitter.onError(it.cause())
          return@getConnection
        }
        val connection = it.result()
        connection.setAutoCommit(false) {
          if (it.failed()) {
            emitter.onError(it.cause())
            return@setAutoCommit
          }
          connection.updateWithParams(
            "Insert into t_ubs_user (code, name, type, email, phone) values (?,?,?,?,?)",
            JsonArray().add(user.code).add(user.name).add(user.type).add(user.email).add(user.phone)
          ) {
            if (it.failed()) {
              connection.rollback {
                emitter.onError(it.cause())
              }
              return@updateWithParams
            }
            connection.query("select last_insert_id() as id") {
              val rs = it.result()
              if (it.failed()) {
                connection.rollback {
                  emitter.onError(it.cause())
                }
                return@query
              }
              connection.commit {
                user.id = rs.results[0].getLong(0)
                emitter.onSuccess(user)
              }
            }
          }
        }
      }
    }
  }

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

  override fun update(user: UserEntity) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun delete(id: Long) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun retrieveOne(id: Long): Single<UserEntity> {
    return Single.create {
      val emitter = it
      sqlClient?.querySingleWithParams(
        "select id, name, code, type, email, phone, email_verified, phone_verified " +
          "from t_ubs_user where id = ? and is_deleted = 0 limit 1", JsonArray().add(id)
      ) {
        if (it.failed()) {
          emitter.onError(it.cause())
        } else {
          val jsonArray = it.result()
          val result = UserEntity(
            jsonArray.getLong(0),
            jsonArray.getString(1),
            jsonArray.getString(2),
            jsonArray.getInteger(3),
            jsonArray.getString(4),
            jsonArray.getString(5),
            !0.equals(jsonArray.getInteger(6)),
            !0.equals(jsonArray.getInteger(7))
          )
          emitter.onSuccess(result)
        }
      }
    }
  }
}
