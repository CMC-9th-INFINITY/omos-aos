package com.infinity.omos.source.local.user

import com.infinity.omos.data.user.UserToken

interface UserLocalDataSource {

    suspend fun saveToken(token: UserToken)
}