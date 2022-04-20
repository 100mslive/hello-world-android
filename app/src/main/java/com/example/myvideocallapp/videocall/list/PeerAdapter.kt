package com.example.myvideocallapp.videocall.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.myvideocallapp.R
import com.example.myvideocallapp.TrackPeerMap
import com.example.myvideocallapp.VideoCallVm
import live.hms.video.media.tracks.HMSAudioTrack

class PeerAdapter : ListAdapter<VideoCallVm.Track, PeerViewHolder>(DIFFUTIL_CALLBACK) {

    companion object {
        val DIFFUTIL_CALLBACK = object : DiffUtil.ItemCallback<VideoCallVm.Track>() {
            override fun areItemsTheSame(
                oldItem: VideoCallVm.Track,
                newItem: VideoCallVm.Track
            ) = oldItem.peer.peerID == newItem.peer.peerID &&
                    if(oldItem is VideoCallVm.Track.AudioTrack && newItem is VideoCallVm.Track.AudioTrack) {
                        oldItem.audioTrack.trackId == newItem.audioTrack.trackId
                    } else if (oldItem is VideoCallVm.Track.Videotrack.Video && newItem is VideoCallVm.Track.Videotrack.Video) {
                        oldItem.videoTrack.trackId == newItem.videoTrack.trackId
                    } else false

            override fun areContentsTheSame(
                oldItem: VideoCallVm.Track,
                newItem: VideoCallVm.Track
            ) = if(oldItem is VideoCallVm.Track.AudioTrack && newItem is VideoCallVm.Track.AudioTrack) {
                oldItem.audioTrack.isMute == newItem.audioTrack.isMute
            } else if (oldItem is VideoCallVm.Track.Videotrack.Video && newItem is VideoCallVm.Track.Videotrack.Video) {
                oldItem.videoTrack.isMute == newItem.videoTrack.isMute
            } else false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_peer_item, parent, false)
        return PeerViewHolder(view, ::getItem)
    }

    override fun onBindViewHolder(holder: PeerViewHolder, position: Int) {
        getItem(position)?.let {
            holder.stopSurfaceView()
            holder.bind(it)
        }
    }

    override fun onViewAttachedToWindow(holder: PeerViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.startSurfaceView()
    }

    override fun onViewDetachedFromWindow(holder: PeerViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.stopSurfaceView()
    }

}