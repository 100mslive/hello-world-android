package com.example.myvideocallapp.api

import com.google.gson.annotations.SerializedName
import java.net.URI
import java.util.*

data class TokenRequestWithCode constructor(
    @SerializedName("code") val code: String,
    @SerializedName("user_id") val userId: String = UUID.randomUUID().toString(),
) {
    constructor(meetingUrl : String) : this(code = getCodeFromMeetingUrl(meetingUrl))
}

private fun getCodeFromMeetingUrl(meetingUrl: String) : String {
    val path = android.net.Uri.parse(meetingUrl).lastPathSegment!!
    return path
}
