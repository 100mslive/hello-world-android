package com.example.myvideocallapp.videocall.list

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
        if (!sinkAdded) {
            itemView.findViewById<SurfaceViewRenderer>(R.id.videoSurfaceView).apply {

                getItem(adapterPosition).videoTrack?.let { hmsVideoTrack ->
                    init(SharedEglContext.context, null)
                    hmsVideoTrack.addSink(this)
                    sinkAdded = true
                }
            }
        }
    }

    fun stopSurfaceView() {
        // If the sink was added, AND there was a video
        //  then remove the sink and release
        itemView.findViewById<SurfaceViewRenderer>(R.id.videoSurfaceView).apply {

            if (sinkAdded && adapterPosition != -1) {
                getItem(adapterPosition).videoTrack?.let {
                    it.removeSink(this)
                    release()
                    sinkAdded = false
                }
            }
        }
    }

    fun bind(peer: TrackPeerMap) {

        if (!sinkAdded) {
            itemView.findViewById<SurfaceViewRenderer>(R.id.videoSurfaceView).apply {
                setEnableHardwareScaler(true)
                setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT)
                sinkAdded = false
            }
        }
        itemView.findViewById<TextView>(R.id.peerName).text = peer.peer.name
    }

}