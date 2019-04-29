package com.juext.artisan.ucs.service

import com.juext.artisan.ucs.handler.domain.user.UserInfo
import io.reactivex.Single

interface UserService {

  fun create(user: UserInfo): Single<UserInfo>

  fun findById(id: Long): Single<UserInfo>
}
