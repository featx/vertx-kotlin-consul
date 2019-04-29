package com.juext.artisan.ucs.service;

import com.juext.artisan.ucs.handler.domain.user.UserInfo
import com.juext.artisan.ucs.transmit.entity.UserEntity
import com.juext.artisan.ucs.transmit.repository.UserRepo
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserServiceImpl : UserService {

  @Inject
  var userRepo: UserRepo? = null

  override fun create(user: UserInfo): Single<UserInfo> {
    return Single.create {
      val emitter = it
      userRepo?.create(this.toUserEntity(user))?.subscribe({
        emitter.onSuccess(fromUserEntity(it!!))
      }, {
        emitter.onError(it)
      })
    }
  }

  override fun findById(id: Long): Single<UserInfo> {
    return userRepo?.retrieveOne(id)?.map {
      fromUserEntity(it)
    }!!
  }

  private fun fromUserEntity(userEntity: UserEntity): UserInfo {
    return UserInfo(
      userEntity.id,
      userEntity.code,
      userEntity.name,
      userEntity.type,
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
      user.email,
      user.phone,
      user.emailVerified,
      user.phoneVerified
    )
  }
}
