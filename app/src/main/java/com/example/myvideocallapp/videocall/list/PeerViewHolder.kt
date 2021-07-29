package com.example.myvideocallapp.videocall.list

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myvideocallapp.R
import com.example.myvideocallapp.TrackPeerMap
import live.hms.video.utils.SharedEglContext
import org.webrtc.RendererCommon
import org.webrtc.SurfaceViewRenderer

class PeerViewHolder(view: View, private val getItem: (Int) -> TrackPeerMap) :
    RecyclerView.ViewHolder(view) {
    private val TAG = PeerViewHolder::class.java.simpleName
    private var sinkAdded = false

    init {
        itemView.findViewById<SurfaceViewRenderer>(R.id.videoSurfaceView).apply {
            setEnableHardwareScaler(true)
            setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT)
        }
    }

    fun startSurfaceView() {
        itemView.findViewById<SurfaceViewRenderer>(R.id.videoSurfaceView).apply {
            init(SharedEglContext.context, null)
            getItem(adapterPosition).videoTrack?.let {
                sinkAdded = true
                it.addSink(this)
            }
        }
    }

    fun bind(peer: TrackPeerMap) {
        if (!sinkAdded) {
            itemView.findViewById<SurfaceViewRenderer>(R.id.videoSurfaceView).apply {
                peer.videoTrack?.let {
                    sinkAdded = true
                    it.addSink(this)
                }
            }
        }

        itemView.findViewById<TextView>(R.id.peerName).text = peer.peer.name
    }

    fun stopSurfaceView() {
        Log.d(TAG, "UNbinding")
        itemView.findViewById<SurfaceViewRenderer>(R.id.videoSurfaceView).apply {

            if (sinkAdded && adapterPosition != -1) {
                getItem(adapterPosition).videoTrack?.removeSink(this)
            }
            release()
            sinkAdded = false
        }
    }
}