package com.example.myvideocallapp.videocall.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.myvideocallapp.R
import live.hms.video.media.tracks.HMSVideoTrack

class PeerAdapter : ListAdapter<HMSVideoTrack, PeerViewHolder>(DIFFUTIL_CALLBACK) {

    companion object {
        val DIFFUTIL_CALLBACK = object : DiffUtil.ItemCallback<HMSVideoTrack>() {
            override fun areItemsTheSame(
                oldItem: HMSVideoTrack,
                newItem: HMSVideoTrack
            ) = oldItem.trackId == newItem.trackId


            override fun areContentsTheSame(
                oldItem: HMSVideoTrack,
                newItem: HMSVideoTrack
            ) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_peer_item, parent, false)
        return PeerViewHolder(view, ::getItem)
    }

    override fun onBindViewHolder(holder: PeerViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onViewAttachedToWindow(holder: PeerViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.startSurfaceView()
    }

    override fun onViewDetachedFromWindow(holder: PeerViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.stopSurfaceView()
    }

    override fun onViewRecycled(holder: PeerViewHolder) {
        super.onViewRecycled(holder)
        holder.stopSurfaceView()
    }
}