package org.featx.cusp.user.transmit.entity

import java.io.Serializable
import java.time.LocalDateTime

data class UserEntity (
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
  var phoneVerified: Boolean? = null,
  var createdAt: LocalDateTime? = null,
  var updatedAt: LocalDateTime? = null
) : Serializable {
  constructor() : this(1)
}
