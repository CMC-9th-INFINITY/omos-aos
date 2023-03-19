package com.infinity.omos.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.infinity.omos.data.user.UserToken
import com.infinity.omos.utils.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LocalModule {

    private val Context.dataStore by preferencesDataStore("settings")

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.dataStore

    @Provides
    @Singleton
    fun provideDataStoreManager(
        dataStore: DataStore<Preferences>
    ): DataStoreManager = DataStoreManager(dataStore)
}