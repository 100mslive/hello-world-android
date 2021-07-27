package com.example.myvideocallapp.api

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TokenResponse(
    @SerializedName("token") val token: String
)
