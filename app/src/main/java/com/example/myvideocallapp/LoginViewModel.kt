package com.example.myvideocallapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myvideocallapp.api.HmsAuthTokenApi
import com.example.myvideocallapp.api.TokenRequestWithCode
import com.example.myvideocallapp.networking.NetworkingClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.URI

class LoginViewModel : ViewModel() {

    val authToken = MutableLiveData<String>(null)
    val loading = MutableLiveData(false)

    fun authenticate(url : String, name : String) {
        viewModelScope.launch {
            loading.postValue(true)
            try {
                val token = getAuthToken(url) // then launch the other activity.
                authToken.postValue(token)
            } finally {
                loading.postValue(false)
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
        Log.d("AuthToken", token.token)
        return@withContext token.token
    }

    private fun getSubdomainForMeetingUrl(meetingUrl : String ) : String {
        return URI(meetingUrl).host
    }
}