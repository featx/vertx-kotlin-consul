package org.featx.cusp.user.handler;

import org.featx.cusp.user.handler.domain.user.UserInfo
import org.featx.cusp.user.service.UserService

import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.http.HttpMethod.GET
import io.vertx.core.http.HttpMethod.POST
import io.vertx.core.json.Json
import io.vertx.ext.web.RoutingContext

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserHandler : Handler<RoutingContext> {
  @Inject
  private var userService: UserService? = null

  override fun handle(rc: RoutingContext?) {
    if (POST.equals(rc?.request()?.method()) && "/user/info".equals(rc?.request()?.path())) {
      rc?.request()?.bodyHandler {
        val userInfo = it.toJsonObject().mapTo(UserInfo::class.java)
        postUserInfo(userInfo).onSuccess {
          rc.response()?.putHeader("content-type", "application/json")
            ?.end(Json.encode(it))
        }.onFailure {
          rc.response()?.putHeader("content-type", "application/json")
            ?.end("{\"success\": false, \"result\": {\"code\": \"\", \"message\": \"\", \"tip\": \"\"}}")
        }
      }
    } else if (GET.equals(rc?.request()?.method()) && "/user/info".equals(rc?.request()?.path())) {
      var id = rc?.request()?.getParam("id")
      if (id == null) {
        id = rc?.request()?.getFormAttribute("id")
      }
      getUserInfo(id?.toLong()!!).onSuccess {
        rc?.response()?.putHeader("content-type", "application/json")
          ?.end(Json.encode(it))
      }.onFailure {
        rc?.response()?.putHeader("content-type", "application/json")
          ?.end("{\"success\": false, \"result\": {\"code\": \"\", \"message\": \"\", \"tip\": \"\"}}")
      }
    }
  }

  fun postUserInfo(user: UserInfo): Future<UserInfo> {
    return this.userService?.create(user)!!
  }

  fun getUserInfo(id: Long): Future<UserInfo> {
    return this.userService?.findById(id)!!
  }
}
