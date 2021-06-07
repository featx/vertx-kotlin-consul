package org.featx.cusp.user.service

import org.featx.cusp.user.handler.domain.user.UserInfo

import io.vertx.core.Future

interface UserService {

  fun create(user: UserInfo): Future<UserInfo>

  fun findById(id: Long): Future<UserInfo>
}
