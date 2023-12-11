package com.ownlab.ownlab_client.utils.Auth

import com.ownlab.ownlab_client.models.LoginResponse
import com.ownlab.ownlab_client.utils.Manager
import com.ownlab.ownlab_client.service.AuthApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import javax.inject.Inject

class AuthAuthenticator @Inject constructor (private val tokenManager: Manager): Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val token = runBlocking {
            tokenManager.get().first()
        }

        return runBlocking {
            val newToken = getToken(token)

            if (!newToken.isSuccessful || newToken.body() == null) {
                tokenManager.delete()
            }

            newToken.body()?.let {
                tokenManager.save(it.token)

                response.request.newBuilder()
                    .header("Authorization", "Bearer ${it.token}")
                    .build()
            }
        }
    }

    private suspend fun getToken(refreshToken: String?): retrofit2.Response<LoginResponse> {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://221.159.102.58:3002/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
        val authApiService = retrofit.create(AuthApi::class.java)

        return authApiService.refreshToken("Bearer $refreshToken")
    }
}