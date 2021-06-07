package org.featx.cusp.user.transmit.repository

import org.featx.cusp.user.transmit.entity.UserEntity
import org.featx.cusp.user.transmit.criteria.UserCriteria

import io.vertx.core.Future
import io.vertx.sqlclient.SqlClient


interface UserRepo: TransactionConnectionHold {

  fun create(user: UserEntity, txConnect: SqlClient?): Future<UserEntity>

  fun update(user: UserEntity, txConnect: SqlClient?): Future<Boolean>

  fun delete(id: Long, txConnect: SqlClient?): Future<Boolean>

  fun retrieveOne(id: Long, txConnect: SqlClient?): Future<UserEntity>

  fun count(criteria: UserCriteria, txConnect: SqlClient?): Future<Long>

  fun page(criteria: UserCriteria, txConnect: SqlClient?): Future<UserEntity>

}
