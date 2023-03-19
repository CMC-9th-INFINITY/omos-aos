package com.infinity.omos.source.local

import com.infinity.omos.data.user.UserToken
import com.infinity.omos.utils.DataStoreManager
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {

    suspend fun saveToken(token: UserToken) {
        dataStoreManager.saveToken(token)
    }
}