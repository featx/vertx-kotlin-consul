package com.juext.artisan.ucs.transmit.entity

import java.io.Serializable

data class UserEntity(
  var id: Long? = null,
  var code: String? = null,
  var name: String? = null,
  var type: Int? = null,
  var email: String? = null,
  var phone: String? = null,
  var emailVerified: Boolean? = null,
  var phoneVerified: Boolean? = null
) : Serializable {
  constructor() : this(1)
}
