package com.example.myvideocallapp

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

const val BUNDLE_AUTH_TOKEN = "100msauthkey"

class VideoCallActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_call)
        val key = intent?.extras?.getString(BUNDLE_AUTH_TOKEN)

        val vm: VideoCallVm by viewModels {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return VideoCallVm(key, application) as T
                }
            }
        }

        vm.joined.observe(this, Observer {
            findViewById<TextView>(R.id.textView).text = "$it"
        })
    }
}