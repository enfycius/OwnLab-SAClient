package com.ownlab.ownlab_client.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import androidx.datastore.preferences.core.Preferences
import com.ownlab.ownlab_client.service.AuthApi
import com.ownlab.ownlab_client.utils.Auth.AuthAuthenticator
import com.ownlab.ownlab_client.utils.Auth.AuthInterceptor
import com.ownlab.ownlab_client.utils.Manager
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_store")

@Module
@InstallIn(SingletonComponent::class)
class SingleModule {
    @Singleton
    @Provides
    fun provideManager(@ApplicationContext context: Context): Manager = Manager(context)

    @Singleton
    @Provides
    fun provideOKHTTPClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(authAuthenticator)
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthAPI(retrofit: Retrofit.Builder): AuthApi =
        retrofit
            .build()
            .create(AuthApi::class.java)

    @Singleton
    @Provides
    fun provideAuthAuthenticator(tokenManager: Manager): AuthAuthenticator =
        AuthAuthenticator(tokenManager)

    @Singleton
    @Provides
    fun provideAuthInterceptor(tokenManager: Manager): AuthInterceptor =
        AuthInterceptor(tokenManager)

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl("http://221.159.102.58:3002/")
            .addConverterFactory(GsonConverterFactory.create())
}