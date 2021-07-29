package com.example.myvideocallapp.videocall.list

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myvideocallapp.R
import live.hms.video.media.tracks.HMSVideoTrack
import live.hms.video.utils.SharedEglContext
import org.webrtc.RendererCommon
import org.webrtc.SurfaceViewRenderer

class PeerViewHolder(view: View, private val getItem: (Int) -> HMSVideoTrack) :
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
            getItem(adapterPosition).addSink(this)
        }
    }

    fun bind(peer: HMSVideoTrack) {

        itemView.findViewById<SurfaceViewRenderer>(R.id.videoSurfaceView).apply {
            peer.addSink(this)
        }

        itemView.findViewById<TextView>(R.id.peerName).text = peer.trackId
    }

    fun stopSurfaceView() {
        Log.d(TAG, "UNbinding")
        itemView.findViewById<SurfaceViewRenderer>(R.id.videoSurfaceView).apply {
            if (adapterPosition != -1) {
                getItem(adapterPosition).removeSink(this)
            }
            release()
        }
    }
}