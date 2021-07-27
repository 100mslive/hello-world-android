package com.example.myvideocallapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myvideocallapp.api.HmsAuthTokenApi
import com.example.myvideocallapp.api.TokenRequestWithCode
import com.example.myvideocallapp.networking.NetworkingClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URI

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getAuthToken("https://aniket.app.100ms.live/preview/snippy-purple-mouse")
    }

    fun getAuthToken(url : String) {
        val subdomain = getSubdomainForMeetingUrl(url)
        val tokenRequest = TokenRequestWithCode(url)

        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val token = NetworkingClient()
                    .providesRetrofit()
                    .create(HmsAuthTokenApi::class.java)
                    .getAuthToken(subdomain, tokenRequest)
                Log.d("AuthToken", token.token)
            }
        }
    }

    fun getSubdomainForMeetingUrl(meetingUrl : String ) : String {
        return URI(meetingUrl).host
    }

}