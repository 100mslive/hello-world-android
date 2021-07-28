package com.example.myvideocallapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import live.hms.video.error.HMSException
import live.hms.video.media.tracks.HMSTrack
import live.hms.video.sdk.HMSSDK
import live.hms.video.sdk.HMSUpdateListener
import live.hms.video.sdk.models.*
import live.hms.video.sdk.models.enums.HMSPeerUpdate
import live.hms.video.sdk.models.enums.HMSRoomUpdate
import live.hms.video.sdk.models.enums.HMSTrackUpdate

class VideoCallVm(authKey: String?, application: Application) : AndroidViewModel(application),
    HMSUpdateListener {

    private val TAG = "VideoCallVm"
    val joined = MutableLiveData(false)

    private val hmsSdk = HMSSDK
        .Builder(application)
        .build()

    init {
        if (authKey != null) {
            Log.d(TAG, "Joining with $authKey")
            hmsSdk.join(HMSConfig("name", authKey), this)
        }
    }

    override fun onError(error: HMSException) {
        TODO("Not yet implemented")
    }

    override fun onJoin(room: HMSRoom) {
        // Room joined.
        joined.postValue(true)
    }

    override fun onMessageReceived(message: HMSMessage) {
        TODO("Not yet implemented")
    }

    override fun onPeerUpdate(type: HMSPeerUpdate, peer: HMSPeer) {
        TODO("Not yet implemented")
    }

    override fun onRoleChangeRequest(request: HMSRoleChangeRequest) {
        TODO("Not yet implemented")
    }

    override fun onRoomUpdate(type: HMSRoomUpdate, hmsRoom: HMSRoom) {
        TODO("Not yet implemented")
    }

    override fun onTrackUpdate(type: HMSTrackUpdate, track: HMSTrack, peer: HMSPeer) {
        TODO("Not yet implemented")
    }


}