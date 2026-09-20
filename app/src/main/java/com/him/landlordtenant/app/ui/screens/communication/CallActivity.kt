package com.him.landlordtenant.app.ui.screens.communication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.him.landlordtenant.app.webrtc.SignalingClient
import com.him.landlordtenant.app.webrtc.WebRTCClient
import org.webrtc.*

class CallActivity : ComponentActivity() {

    private var webRtcClient: WebRTCClient? = null
    private var signalingClient: SignalingClient? = null
    private var isVideoCall = true
    private var callId = ""
    private var isCaller = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        callId = intent.getStringExtra("CALL_ID") ?: ""
        isVideoCall = intent.getBooleanExtra("IS_VIDEO_CALL", true)
        isCaller = intent.getBooleanExtra("IS_CALLER", false)

        setContent {
            CallScreenContent()
        }
    }

    @Composable
    fun CallScreenContent() {
        var remoteRenderer by remember { mutableStateOf<SurfaceViewRenderer?>(null) }
        var localRenderer by remember { mutableStateOf<SurfaceViewRenderer?>(null) }

        LaunchedEffect(remoteRenderer, localRenderer) {
            if (remoteRenderer != null && localRenderer != null) {
                initWebRTC(remoteRenderer!!, localRenderer!!)
            }
        }

        Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
            // Remote Video
            AndroidView(
                factory = { context ->
                    SurfaceViewRenderer(context).also { remoteRenderer = it }
                },
                modifier = Modifier.fillMaxSize()
            )

            // Local Video (Overlay)
            AndroidView(
                factory = { context ->
                    SurfaceViewRenderer(context).also { localRenderer = it }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(120.dp, 180.dp)
            )

            // End Call Button
            FloatingActionButton(
                onClick = { 
                    webRtcClient?.close()
                    finish()
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
                containerColor = Color.Red,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.CallEnd, contentDescription = "End Call")
            }
        }
    }

    private fun initWebRTC(remoteView: SurfaceViewRenderer, localView: SurfaceViewRenderer) {
        val iceServers = listOf(
            PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer()
        )

        val observer = object : PeerConnection.Observer {
            override fun onIceCandidate(candidate: IceCandidate?) {
                candidate?.let { signalingClient?.sendIceCandidate(it, isCaller) }
            }

            override fun onAddStream(stream: MediaStream?) {
                if (stream?.videoTracks?.isNotEmpty() == true) {
                    stream.videoTracks[0].addSink(remoteView)
                }
            }

            override fun onSignalingChange(p0: PeerConnection.SignalingState?) {}
            override fun onIceConnectionChange(p0: PeerConnection.IceConnectionState?) {}
            override fun onIceConnectionReceivingChange(p0: Boolean) {}
            override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState?) {}
            override fun onIceCandidatesRemoved(p0: Array<out IceCandidate>?) {}
            override fun onRemoveStream(p0: MediaStream?) {}
            override fun onDataChannel(p0: DataChannel?) {}
            override fun onRenegotiationNeeded() {}
        }

        webRtcClient = WebRTCClient(this, observer)
        webRtcClient?.initializePeerConnection(iceServers)
        webRtcClient?.initRemoteSurfaceView(remoteView)
        webRtcClient?.startLocalVideo(localView, isVideoCall)

        signalingClient = SignalingClient(callId, object : SignalingClient.SignalingListener {
            override fun onOfferReceived(description: SessionDescription) {
                if (!isCaller) {
                    webRtcClient?.peerConnection?.setRemoteDescription(object : SdpObserver {
                        override fun onCreateSuccess(p0: SessionDescription?) {}
                        override fun onSetSuccess() {
                            webRtcClient?.answer(object : SdpObserver {
                                override fun onCreateSuccess(desc: SessionDescription?) {
                                    desc?.let { signalingClient?.sendAnswer(it) }
                                }
                                override fun onSetSuccess() {}
                                override fun onCreateFailure(p0: String?) {}
                                override fun onSetFailure(p0: String?) {}
                            })
                        }
                        override fun onCreateFailure(p0: String?) {}
                        override fun onSetFailure(p0: String?) {}
                    }, description)
                }
            }

            override fun onAnswerReceived(description: SessionDescription) {
                if (isCaller) {
                    webRtcClient?.peerConnection?.setRemoteDescription(object : SdpObserver {
                        override fun onCreateSuccess(p0: SessionDescription?) {}
                        override fun onSetSuccess() {}
                        override fun onCreateFailure(p0: String?) {}
                        override fun onSetFailure(p0: String?) {}
                    }, description)
                }
            }

            override fun onIceCandidateReceived(candidate: IceCandidate) {
                webRtcClient?.peerConnection?.addIceCandidate(candidate)
            }
        })

        signalingClient?.observeSignaling(isCaller)

        if (isCaller) {
            webRtcClient?.call(object : SdpObserver {
                override fun onCreateSuccess(desc: SessionDescription?) {
                    desc?.let { signalingClient?.sendOffer(it) }
                }
                override fun onSetSuccess() {}
                override fun onCreateFailure(p0: String?) {}
                override fun onSetFailure(p0: String?) {}
            })
        }
    }

    override fun onDestroy() {
        webRtcClient?.close()
        super.onDestroy()
    }
}
