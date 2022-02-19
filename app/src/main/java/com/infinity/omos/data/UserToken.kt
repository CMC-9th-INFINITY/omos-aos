package com.infinity.omos.data

data class UserToken(
    var accesstToken: String?,
    var refreshToken: String?,
    var userId: Long?
)
