package com.example.myvideocallapp.videocall.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myvideocallapp.R
import com.example.myvideocallapp.TrackPeerMap
import com.example.myvideocallapp.VideoCallVm
import live.hms.video.utils.SharedEglContext
import org.webrtc.RendererCommon
import org.webrtc.SurfaceViewRenderer

class PeerViewHolder(view: View, private val getItem: (Int) -> VideoCallVm.Track) :
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

                val item = getItem(adapterPosition)
                when(item) {
                    is VideoCallVm.Track.AudioTrack -> {} // Ignore audio tracks
                    // Screenshare and videos are treated the same
                    is VideoCallVm.Track.Videotrack -> {
                        init(SharedEglContext.context, null)
                        item.videoTrack.addSink(this)
                        sinkAdded = true
                    }
                }
            }
        }
    }

    fun stopSurfaceView() {
        // If the sink was added, AND there was a video
        //  then remove the sink and release
        itemView.findViewById<SurfaceViewRenderer>(R.id.videoSurfaceView).apply {

            if (sinkAdded && adapterPosition != -1) {
                val item = getItem(adapterPosition)
                if(item is VideoCallVm.Track.Videotrack) {
                    item.videoTrack.removeSink(this)
                    release()
                    sinkAdded = false
                }
            }
        }
    }

    fun bind(trackPeerMap: VideoCallVm.Track) {

        if (!sinkAdded) {
            itemView.findViewById<SurfaceViewRenderer>(R.id.videoSurfaceView).apply {
                setEnableHardwareScaler(true)
                setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT)
                sinkAdded = false
            }
        }
        itemView.findViewById<TextView>(R.id.peerName).text = trackPeerMap.peer.name
    }

}