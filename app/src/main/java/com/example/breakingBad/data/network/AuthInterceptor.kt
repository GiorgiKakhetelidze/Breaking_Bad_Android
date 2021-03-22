package com.example.breakingBad.data.network

import com.example.breakingBad.data.storage.DataStore
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        DataStore.authToken?.let {
            builder.addHeader("Authorization", "Bearer ${DataStore.authToken}")
        }

        return chain.proceed(builder.build())
    }
}