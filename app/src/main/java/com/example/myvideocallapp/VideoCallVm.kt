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
import live.hms.video.sdk.models.trackchangerequest.HMSChangeTrackStateRequest

data class TrackPeerMap(
    val videoTrack: HMSVideoTrack?,
    val peer: HMSPeer
)

class VideoCallVm(name: String?, authToken: String?, application: Application) :
    AndroidViewModel(application),
    HMSUpdateListener {

    private val _videoCallParticipants = MutableLiveData<List<TrackPeerMap>>(emptyList())
    val videoCallParticipants: LiveData<List<TrackPeerMap>> = _videoCallParticipants
    private val _receivedMessages : MutableLiveData<List<HMSMessage>> = MutableLiveData(emptyList())
    val receivedMessages : LiveData<List<HMSMessage>> = _receivedMessages

    private val TAG = "VideoCallVm"

    private val hmsSdk = HMSSDK
        .Builder(application)
        .build()

    init {
        if (authToken != null && name != null) {
            Log.d(TAG, "Joining with $authToken")
            hmsSdk.join(HMSConfig(name, authToken), this)
        } else {
            Log.e(TAG, "Neither auth token nor name can be null.")
        }
    }

    override fun onJoin(room: HMSRoom) {
        _videoCallParticipants.postValue(getCurrentParticipants())
    }

    override fun onTrackUpdate(type: HMSTrackUpdate, track: HMSTrack, peer: HMSPeer) {
        _videoCallParticipants.postValue(getCurrentParticipants())
    }

    override fun onPeerUpdate(type: HMSPeerUpdate, peer: HMSPeer) {
        _videoCallParticipants.postValue(getCurrentParticipants())
    }

    override fun onMessageReceived(message: HMSMessage) {
        // Being careful to create a new list with `plus`
        //  because mutating a livedata value can result it not sending updates
        //  to the observer.
        _receivedMessages.postValue(receivedMessages.value!!.plus(message))
    }
    override fun onRoleChangeRequest(request: HMSRoleChangeRequest) { TODO("Not yet implemented, look at the advanced sample app for details") }
    override fun onRoomUpdate(type: HMSRoomUpdate, hmsRoom: HMSRoom) {}
    override fun onChangeTrackStateRequest(details: HMSChangeTrackStateRequest) { TODO("Not yet implemented, look at the advanced sample app for details") }

    override fun onError(error: HMSException) {
        Log.d(TAG, "Error $error")
    }

    private fun getCurrentParticipants(): List<TrackPeerMap> {
        // Convert all the peers into a map of them and their tracks.
        val trackAndPeerMap = hmsSdk.getPeers().flatMap {
            val screenShare = it.auxiliaryTracks.find { auxTrack -> auxTrack is HMSVideoTrack }
            if (screenShare is HMSVideoTrack) {
                listOf(TrackPeerMap(it.videoTrack, it), TrackPeerMap(screenShare, it))
            } else {
                listOf(TrackPeerMap(it.videoTrack, it))
            }
        }

        return trackAndPeerMap
    }
}