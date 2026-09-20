package com.him.landlordtenant.app.telecom

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.telecom.PhoneAccount
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager

object TelecomHelper {

    private const val ACCOUNT_ID = "CHAT_APP_VOIP_ACCOUNT"

    fun getPhoneAccountHandle(context: Context): PhoneAccountHandle {
        val componentName = ComponentName(context, CallConnectionService::class.java)
        return PhoneAccountHandle(componentName, ACCOUNT_ID)
    }

    fun registerPhoneAccount(context: Context) {
        val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        val handle = getPhoneAccountHandle(context)

        val phoneAccount = PhoneAccount.builder(handle, "ChatApp Calls")
            .setCapabilities(PhoneAccount.CAPABILITY_SELF_MANAGED)
            .build()

        telecomManager.registerPhoneAccount(phoneAccount)
    }

    fun triggerIncomingCall(context: Context, callId: String, callerName: String) {
        val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        val handle = getPhoneAccountHandle(context)

        val extras = Bundle().apply {
            putString("CALL_ID", callId)
            putString("CALLER_NAME", callerName)
            putParcelable(TelecomManager.EXTRA_INCOMING_CALL_ADDRESS, Uri.fromParts("tel", callId, null))
        }

        telecomManager.addNewIncomingCall(handle, extras)
    }
}
