package com.ownlab.ownlab_client.utils.Auth

import com.ownlab.ownlab_client.utils.Manager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor (private val tokenManager: Manager): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenManager.get().first()
        }

        val request = chain.request().newBuilder()
        request.addHeader("Authorization", "Bearer $token")

        return chain.proceed(request.build())
    }
}