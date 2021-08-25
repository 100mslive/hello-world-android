package com.example.myvideocallapp.api

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface HmsAuthTokenApi {

    @POST("https://prod-in.100ms.live/hmsapi/get-token")
    suspend fun getAuthToken(
        @Header("subdomain") subdomain: String,
        @Body tokenRequestWithCode: TokenRequestWithCode
    ): TokenResponse

    @POST
    suspend fun getAuthToken(
        @Url url: String,
        @Body tokenRequestWithRoomId: TokenRequestWithRoomId
    ): TokenResponse
}