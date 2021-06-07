package org.featx.cusp.user.service;

import org.featx.cusp.user.handler.domain.user.UserInfo
import org.featx.cusp.user.transmit.entity.UserEntity
import org.featx.cusp.user.transmit.repository.UserRepo
import io.vertx.core.Future
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserServiceImpl : UserService {

  @Inject
  private lateinit var userRepo: UserRepo

  override fun create(user: UserInfo): Future<UserInfo> {
    return userRepo.connectionPool.withTransaction {
      userRepo.create(toUserEntity(user), it).map(this::fromUserEntity)
    }
  }

  override fun findById(id: Long): Future<UserInfo> {
    return userRepo?.retrieveOne(id, null)?.map {
      fromUserEntity(it)
    }!!
  }

  private fun fromUserEntity(userEntity: UserEntity): UserInfo {
    return UserInfo(
      userEntity.id,
      userEntity.code,
      userEntity.name,
      userEntity.type,
      userEntity.username,
      userEntity.password,
      userEntity.enable,
      userEntity.avatar,
      userEntity.email,
      userEntity.phone,
      userEntity.emailVerified,
      userEntity.phoneVerified
    )
  }

  private fun toUserEntity(user: UserInfo): UserEntity {
    return UserEntity(
      user.id,
      user.code,
      user.name,
      user.type,
      user.username,
      user.password,
      user.enable,
      user.avatar,
      user.email,
      user.phone,
      user.emailVerified,
      user.phoneVerified
    )
  }
}
