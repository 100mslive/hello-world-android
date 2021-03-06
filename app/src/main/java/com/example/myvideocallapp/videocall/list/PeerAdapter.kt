package com.example.myvideocallapp.videocall.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.myvideocallapp.R
import com.example.myvideocallapp.TrackPeerMap

class PeerAdapter : ListAdapter<TrackPeerMap, PeerViewHolder>(DIFFUTIL_CALLBACK) {

    companion object {
        val DIFFUTIL_CALLBACK = object : DiffUtil.ItemCallback<TrackPeerMap>() {
            override fun areItemsTheSame(
                oldItem: TrackPeerMap,
                newItem: TrackPeerMap
            ) = oldItem.peer.peerID == newItem.peer.peerID &&
                    oldItem.videoTrack?.trackId == newItem.videoTrack?.trackId

            override fun areContentsTheSame(
                oldItem: TrackPeerMap,
                newItem: TrackPeerMap
            ) = oldItem.videoTrack?.isMute == newItem.videoTrack?.isMute
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