package com.infinity.omos.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.infinity.omos.data.user.UserToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class DataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val accessTokenKey = stringPreferencesKey("accessToken")
    private val refreshTokenKey = stringPreferencesKey("refreshToken")
    private val userIdKey = intPreferencesKey("userId")

    val tokenFlow = dataStore.data.map { settings ->
        UserToken(
            settings[accessTokenKey] ?: "",
            settings[refreshTokenKey] ?: "",
            settings[userIdKey] ?: -1
        )
    }

    suspend fun saveToken(token: UserToken) {
        dataStore.edit { settings ->
            settings[accessTokenKey] = token.accessToken
            settings[refreshTokenKey] = token.refreshToken
            settings[userIdKey] = token.userId
        }
    }

    fun getUserId(): Int {
        return runBlocking { tokenFlow.first().userId }
    }

    fun isExistToken(): Boolean {
        val userId = getUserId()
        return userId != -1
    }
}