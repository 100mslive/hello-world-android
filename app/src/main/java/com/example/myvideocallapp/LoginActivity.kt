package com.example.myvideocallapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val vm : LoginViewModel by viewModels()

        vm.authToken.observe(this, { authToken ->
            launchVideoRoomActivity(authToken)
        })

        vm.loading.observe(this, {isLoading ->
            setLoading(isLoading)
        })
    }

    private fun launchVideoRoomActivity(authToken : String) {

    }

    private fun setLoading(isLoading : Boolean) {

    }
}