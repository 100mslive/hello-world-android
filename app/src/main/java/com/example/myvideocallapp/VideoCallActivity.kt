package com.example.myvideocallapp

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myvideocallapp.videocall.list.PeerAdapter

const val BUNDLE_AUTH_TOKEN = "100msauthkey"

class VideoCallActivity : AppCompatActivity() {
    private val TAG = VideoCallActivity::class.java.simpleName
    private val peerAdapter = PeerAdapter()

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

        findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = GridLayoutManager(this@VideoCallActivity, 2)
            adapter = peerAdapter
        }

        vm.roomState.observe(this, Observer {
            when (it) {
                RoomState.Loading -> Log.d(TAG, "is loading")
                is RoomState.Joined -> {
                    Log.d(TAG, "peers are ${it.peers.fold("") { msg, peer -> msg + peer.name }}")
                    peerAdapter.submitList(it.peers)
                }
            }
        })
    }
}