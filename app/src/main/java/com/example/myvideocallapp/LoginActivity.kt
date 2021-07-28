package com.example.myvideocallapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val vm : LoginViewModel by viewModels()

        vm.authToken.observe(this, { authToken ->
            launchVideoRoomActivity(authToken)
        })

        vm.loading.observe(this, { isLoading ->
            setLoading(isLoading)
        })

        vm.error.observe(this, { error -> showError(error) })

        findViewById<Button>(R.id.authenticateButton).setOnClickListener {
            vm.authenticate("https://aniket.app.100ms.live/preview/snippy-purple-mouse", "cat")
        }
    }

    private fun launchVideoRoomActivity(authToken : String?) {
        if(authToken != null) {
            // Launch the video room
            startActivity(Intent(this, VideoCallActivity::class.java).apply {
                putExtra(BUNDLE_AUTH_TOKEN, authToken)
            })
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