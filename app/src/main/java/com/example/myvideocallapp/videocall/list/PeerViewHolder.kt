package com.example.myvideocallapp.videocall.list

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myvideocallapp.R
import live.hms.video.sdk.models.HMSPeer
import live.hms.video.utils.SharedEglContext
import org.webrtc.RendererCommon
import org.webrtc.SurfaceViewRenderer

class PeerViewHolder(view: View, private val getItem: (Int) -> HMSPeer) :
    RecyclerView.ViewHolder(view) {
    private val TAG = PeerViewHolder::class.java.simpleName

    init {
        itemView.findViewById<SurfaceViewRenderer>(R.id.videoSurfaceView).apply {
            setEnableHardwareScaler(true)
            setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT)
        }
    }

    fun startSurfaceView() {
        itemView.findViewById<SurfaceViewRenderer>(R.id.videoSurfaceView).apply {
            init(SharedEglContext.context, null)
        }
    }

    fun bind(peer: HMSPeer) {
        itemView.findViewById<SurfaceViewRenderer>(R.id.videoSurfaceView).apply {
            peer.videoTrack?.addSink(this)
        }

        itemView.findViewById<TextView>(R.id.peerName).text = peer.name
    }

    fun stopSurfaceView() {
        Log.d(TAG, "UNbinding")
        itemView.findViewById<SurfaceViewRenderer>(R.id.videoSurfaceView).apply {
            if (adapterPosition != -1) {
                getItem(adapterPosition).videoTrack?.removeSink(this)
            }
            release()
        }
    }
}