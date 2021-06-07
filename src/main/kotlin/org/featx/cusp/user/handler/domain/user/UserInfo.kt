package org.featx.cusp.user.handler.domain.user

import java.io.Serializable

data class UserInfo(
  var id: Long? = null,
  var code: String? = null,
  var name: String? = null,
  var type: Int? = null,
  var username: String? = null,
  var password: String? = null,
  var enable: Boolean? = null,
  var avatar: String? = null,
  var email: String? = null,
  var phone: String? = null,
  var emailVerified: Boolean? = null,
  var phoneVerified: Boolean? = null
) : Serializable
