package com.juext.artisan.ucs.handler;

import com.juext.artisan.ucs.handler.domain.user.UserInfo
import com.juext.artisan.ucs.service.UserService
import io.reactivex.Single
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
        postUserInfo(userInfo).subscribe({
          rc.response()?.putHeader("content-type", "application/json")
            ?.end(Json.encode(it))
        }, {
          rc.response()?.putHeader("content-type", "application/json")
            ?.end("{\"success\": false, \"result\": {\"code\": \"\", \"message\": \"\", \"tip\": \"\"}}")
        })
      }
    } else if (GET.equals(rc?.request()?.method()) && "/user/info".equals(rc?.request()?.path())) {
      var id = rc?.request()?.getParam("id")
      if (id == null) {
        id = rc?.request()?.getFormAttribute("id")
      }
      getUserInfo(id?.toLong()!!).subscribe({
        rc?.response()?.putHeader("content-type", "application/json")
          ?.end(Json.encode(it))
      }, {
        rc?.response()?.putHeader("content-type", "application/json")
          ?.end("{\"success\": false, \"result\": {\"code\": \"\", \"message\": \"\", \"tip\": \"\"}}")
      })
    }
//      var username = rc?.request()?.params()?.get("username")
//      if (username == null || username.trim().equals("") ) {
//        username = rc?.request()?.getFormAttribute("username")
//      }
//      if (username != null && !username.trim().equals("") ) {
//        result = create(username)
//      }
  }

  fun postUserInfo(user: UserInfo): Single<UserInfo> {
    return this.userService?.create(user)!!
  }

  fun getUserInfo(id: Long): Single<UserInfo> {
    return this.userService?.findById(id)!!
  }
}
