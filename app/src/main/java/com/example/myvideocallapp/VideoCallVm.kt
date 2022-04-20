package com.example.myvideocallapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import live.hms.video.error.HMSException
import live.hms.video.media.tracks.HMSAudioTrack
import live.hms.video.media.tracks.HMSTrack
import live.hms.video.media.tracks.HMSTrackSource
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

    private val _videoCallParticipants = MutableLiveData<List<Track>>(emptyList())
    val videoCallParticipants: LiveData<List<Track>> = _videoCallParticipants

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

    override fun onMessageReceived(message: HMSMessage) {}
    override fun onRoleChangeRequest(request: HMSRoleChangeRequest) { TODO("Not yet implemented, look at the advanced sample app for details") }
    override fun onRoomUpdate(type: HMSRoomUpdate, hmsRoom: HMSRoom) {}
    override fun onChangeTrackStateRequest(details: HMSChangeTrackStateRequest) { TODO("Not yet implemented, look at the advanced sample app for details") }

    override fun onError(error: HMSException) {
        Log.d(TAG, "Error $error")
    }

    data class PeerRepresentation(
        val peer : HMSPeer,
        val type : Track
    )

    sealed class Track() {
        abstract val peer : HMSPeer
        sealed class Videotrack() : Track() {
            abstract val videoTrack: HMSVideoTrack
            data class ScreenShare(override val videoTrack: HMSVideoTrack, override val peer : HMSPeer) : Videotrack()
            data class Video(override val videoTrack: HMSVideoTrack, override val peer : HMSPeer) : Videotrack()
        }
        data class AudioTrack(val audioTrack : HMSAudioTrack, override val peer: HMSPeer) : Track()
    }

    private fun getCurrentParticipants(): List<Track> {
        // Actually all you need is

        // Convert all the peers into a map of them and their tracks.
        val trackAndPeerMap = hmsSdk.getPeers().flatMap { peer ->

            peer.getAllTracks()
                .filterIsInstance<HMSVideoTrack>()
                .mapNotNull { track ->
                if (track.source == HMSTrackSource.SCREEN) {
                    Track.Videotrack.ScreenShare(track, peer)
                } else if (track.source == HMSTrackSource.REGULAR) {
                    Track.Videotrack.Video(track, peer)
                } else null
            }
        }

        return trackAndPeerMap
    }
}