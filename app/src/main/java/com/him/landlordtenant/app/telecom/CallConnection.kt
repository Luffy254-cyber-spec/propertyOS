package com.him.landlordtenant.app.telecom

import android.content.Context
import android.content.Intent
import android.telecom.Connection
import android.telecom.DisconnectCause
import com.him.landlordtenant.app.ui.screens.communication.CallActivity

class CallConnection(
    private val context: Context,
    private val callId: String
) : Connection() {

    override fun onAnswer() {
        super.onAnswer()
        setActive()
        
        val intent = Intent(context, CallActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("CALL_ID", callId)
            putExtra("IS_VIDEO_CALL", true)
        }
        context.startActivity(intent)
    }

    override fun onReject() {
        super.onReject()
        setDisconnected(DisconnectCause(DisconnectCause.REJECTED))
        destroy()
    }

    override fun onDisconnect() {
        super.onDisconnect()
        setDisconnected(DisconnectCause(DisconnectCause.LOCAL))
        destroy()
    }
}
