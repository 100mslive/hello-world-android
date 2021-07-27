package com.example.myvideocallapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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

        vm.error.observe(this, {error -> showError(error)})
    }

    private fun launchVideoRoomActivity(authToken : String?) {
        if(authToken != null) {
            // Launch the video room
        }
    }

    private fun setLoading(isLoading : Boolean) {

    }

    private fun showError(error : String?) {
        if(error != null ) {
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }
    }
}