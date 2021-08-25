package com.example.myvideocallapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myvideocallapp.api.HmsAuthTokenApi
import com.example.myvideocallapp.api.TokenRequestWithCode
import com.example.myvideocallapp.api.TokenRequestWithRoomId
import com.example.myvideocallapp.networking.NetworkingClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URI
import java.util.*

class LoginViewModel : ViewModel() {
    private val TAG = "LoginViewModel"

    val authToken = MutableLiveData<String?>(null)
    val error = MutableLiveData<String?>(null)

    fun authenticate(url: String) {
        viewModelScope.launch {
            try {
//                val token = getAuthToken(url) // then launch the other activity.
                val token = getAuthToken(
                    tokenEndpoint = "https://prod-in.100ms.live/hmsapi/aniket.app.100ms.live/api/token",
                    roomId = "60fe6102574fe6920b25623c",
                    role = "host"
                )
                authToken.postValue(token)
            } catch (e: Exception) {
                error.postValue(e.message)
                Log.d(TAG, "An error occurred while getting the auth token $e")
            }
        }
    }

    private suspend fun getAuthToken(url : String) : String = withContext(Dispatchers.IO) {
        val subdomain = getSubdomainForMeetingUrl(url)
        val tokenRequest = TokenRequestWithCode(url)

        val token = NetworkingClient()
            .providesRetrofit()
            .create(HmsAuthTokenApi::class.java)
            .getAuthToken(subdomain, tokenRequest)
        Log.d("AuthToken", "Token is ${token.token}")
        return@withContext token.token
    }

    private suspend fun getAuthToken(
        tokenEndpoint: String,
        roomId: String,
        role: String,
        userId: String = UUID.randomUUID().toString()
    ): String = withContext(Dispatchers.IO) {

        val tokenRequest = TokenRequestWithRoomId(roomId, userId, role)

        val token = NetworkingClient()
            .providesRetrofit()
            .create(HmsAuthTokenApi::class.java)
            .getAuthToken(tokenEndpoint, tokenRequest)
        Log.d("AuthToken", "Token is ${token.token}")
        return@withContext token.token
    }

    private fun getSubdomainForMeetingUrl(meetingUrl: String): String {
        return URI(meetingUrl).host
    }
}