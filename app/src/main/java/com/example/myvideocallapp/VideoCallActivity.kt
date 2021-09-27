package com.example.myvideocallapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myvideocallapp.videocall.list.PeerAdapter

const val BUNDLE_AUTH_TOKEN = "100ms-auth-token-bundle-key"
const val BUNDLE_NAME = "100ms-user-name-bundle-key"

class VideoCallActivity : AppCompatActivity() {
    private val TAG = VideoCallActivity::class.java.simpleName
    private val peerAdapter = PeerAdapter()
    private val NUM_PEERS_PER_ROW = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_call)
        val authToken = intent?.extras?.getString(BUNDLE_AUTH_TOKEN)
        val name = intent?.extras?.getString(BUNDLE_NAME)

        val vm: VideoCallVm by viewModels {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return VideoCallVm(name, authToken, application) as T
                }
            }
        }

        findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = GridLayoutManager(this@VideoCallActivity, NUM_PEERS_PER_ROW)
            adapter = peerAdapter
        }

        vm.videoCallParticipants.observe(this, {
            peerAdapter.submitList(it)
        })

        vm.receivedMessages.observe(this, {
            if(it != null && it.size > 0) {
                Toast.makeText(
                    this,
                    "Total messages ${it.size}, last: ${it.lastOrNull()?.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}