package com.example.myvideocallapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import live.hms.video.error.HMSException
import live.hms.video.media.tracks.HMSTrack
import live.hms.video.media.tracks.HMSVideoTrack
import live.hms.video.sdk.HMSSDK
import live.hms.video.sdk.HMSUpdateListener
import live.hms.video.sdk.models.*
import live.hms.video.sdk.models.enums.HMSPeerUpdate
import live.hms.video.sdk.models.enums.HMSRoomUpdate
import live.hms.video.sdk.models.enums.HMSTrackUpdate

data class TrackPeerMap(
    val videoTrack: HMSVideoTrack?,
    val peer: HMSPeer
)

/**
 * This will be observed by the activity to decide whether to show the
 * room or not.
 */
sealed class RoomState {
    object Loading : RoomState()
    data class Joined(val peerTracks: List<TrackPeerMap>) : RoomState()
}

class VideoCallVm(authToken: String?, application: Application) : AndroidViewModel(application),
    HMSUpdateListener {

    private val _roomState = MutableLiveData<RoomState>(RoomState.Loading)
    val roomState: LiveData<RoomState> = _roomState

    private val TAG = "VideoCallVm"

    private val hmsSdk = HMSSDK
        .Builder(application)
        .build()

    init {
        if (authToken != null) {
            Log.d(TAG, "Joining with $authToken")
            hmsSdk.join(HMSConfig("name", authToken), this)
        } else {
            Log.e(TAG, "There was an error since the auth token was null.")
        }
    }

    private fun getCurrentRoomState(): RoomState.Joined {
        // Convert all the peers into a map of them and their tracks.
        val trackAndPeerMap = hmsSdk.getPeers().flatMap {
            val screenShare = it.auxiliaryTracks.find { auxTrack -> auxTrack is HMSVideoTrack }
            if (screenShare is HMSVideoTrack) {
                listOf(TrackPeerMap(it.videoTrack, it), TrackPeerMap(screenShare, it))
            } else {
                listOf(TrackPeerMap(it.videoTrack, it))
            }
        }

        return RoomState.Joined(trackAndPeerMap)
    }


    override fun onError(error: HMSException) {
        Log.d(TAG, "Error $error")
    }

    override fun onJoin(room: HMSRoom) {
        // You joined the room.
        _roomState.postValue(getCurrentRoomState())
    }

    override fun onTrackUpdate(type: HMSTrackUpdate, track: HMSTrack, peer: HMSPeer) {
        // A peer has changed their video or audio sources.
        // Might be starting a screenshare, muted or unmuted their audio or video.
        // Note: The first time you join a chat, the peer joins before their video track is added.
        // onTrackUpdate will always be called right after they join to get their video.
        _roomState.postValue(getCurrentRoomState())
    }

    override fun onPeerUpdate(type: HMSPeerUpdate, peer: HMSPeer) {
        // A peer has joined or left.
        _roomState.postValue(getCurrentRoomState())
    }

    override fun onMessageReceived(message: HMSMessage) {
        // A chat message was received.
    }

    override fun onRoleChangeRequest(request: HMSRoleChangeRequest) {
    }

    override fun onRoomUpdate(type: HMSRoomUpdate, hmsRoom: HMSRoom) {
    }

}