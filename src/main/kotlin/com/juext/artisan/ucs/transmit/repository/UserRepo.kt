package com.juext.artisan.ucs.transmit.repository

import com.juext.artisan.ucs.transmit.entity.UserEntity
import io.reactivex.Single

interface UserRepo {

  fun create(user: UserEntity): Single<UserEntity>

  fun update(user: UserEntity)

  fun delete(id: Long)

  fun retrieveOne(id: Long): Single<UserEntity>

}
