package com.example.myvideocallapp.networking

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkingClient {

    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://does-not-matter.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
        )
            .build()
    }
//
//    fun providesOkHttpClient(): OkHttpClient {
//        val client = OkHttpClient.Builder()
//            .connectTimeout(10, TimeUnit.SECONDS)
//            .writeTimeout(30, TimeUnit.SECONDS)
//            .readTimeout(10, TimeUnit.SECONDS)
//
//        if (BuildConfig.DEBUG) {
//            val logging = HttpLoggingInterceptor()
//            logging.level = HttpLoggingInterceptor.Level.BODY
//            client.addInterceptor(logging)
//        }
//
//        return client.build()
//    }
}