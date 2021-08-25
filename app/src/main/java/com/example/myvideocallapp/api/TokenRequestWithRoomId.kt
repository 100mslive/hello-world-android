package com.example.myvideocallapp.api

import com.google.gson.annotations.SerializedName

data class TokenRequestWithRoomId(
    @SerializedName("room_id") val roomId: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("role") val role: String,
)
