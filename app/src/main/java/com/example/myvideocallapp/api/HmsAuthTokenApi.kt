package com.example.myvideocallapp.api

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface HmsAuthTokenApi {

    @POST("https://prod-in.100ms.live/hmsapi/get-token")
    suspend fun getAuthToken(@Header("subdomain") subdomain : String,
                             @Body tokenRequestWithCode: TokenRequestWithCode
    ) : TokenResponse
}