package com.him.landlordtenant.app.telecom

import android.telecom.Connection
import android.telecom.ConnectionRequest
import android.telecom.ConnectionService
import android.telecom.PhoneAccountHandle

class CallConnectionService : ConnectionService() {

    override fun onCreateIncomingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        val callId = request?.extras?.getString("CALL_ID") ?: ""
        val connection = CallConnection(applicationContext, callId)
        
        connection.setInitializing()
        connection.setAddress(request?.address, android.telecom.TelecomManager.PRESENTATION_ALLOWED)
        connection.setCallerDisplayName(
            request?.extras?.getString("CALLER_NAME") ?: "Unknown Caller",
            android.telecom.TelecomManager.PRESENTATION_ALLOWED
        )
        connection.setActive()
        return connection
    }

    override fun onCreateIncomingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ) {
        super.onCreateIncomingConnectionFailed(connectionManagerPhoneAccount, request)
    }
}
