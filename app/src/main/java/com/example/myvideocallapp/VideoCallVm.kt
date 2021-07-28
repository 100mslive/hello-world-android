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
        Log.d(TAG, "Error $error")
    }

    override fun onJoin(room: HMSRoom) {
        // Room joined.
        joined.postValue(true)
    }

    override fun onMessageReceived(message: HMSMessage) {
    }

    override fun onPeerUpdate(type: HMSPeerUpdate, peer: HMSPeer) {
        Log.d(TAG, "Peer ${peer.name} -> $type")
    }

    override fun onRoleChangeRequest(request: HMSRoleChangeRequest) {
    }

    override fun onRoomUpdate(type: HMSRoomUpdate, hmsRoom: HMSRoom) {
    }

    override fun onTrackUpdate(type: HMSTrackUpdate, track: HMSTrack, peer: HMSPeer) {

    }


}