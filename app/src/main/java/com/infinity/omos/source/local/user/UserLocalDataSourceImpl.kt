package com.infinity.omos.source.local.user

import com.infinity.omos.data.user.UserToken
import com.infinity.omos.utils.DataStoreManager
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : UserLocalDataSource {

    override suspend fun saveToken(token: UserToken) {
        dataStoreManager.saveToken(token)
    }
}