package com.him.landlordtenant.app.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object PhoneUtils {
    fun makeCall(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        context.startActivity(intent)
    }

    fun sendSms(context: Context, phoneNumber: String, message: String = "") {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$phoneNumber")
            putExtra("sms_body", message)
        }
        context.startActivity(intent)
    }

    fun openWhatsApp(context: Context, phoneNumber: String, message: String = "") {
        val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}"
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        context.startActivity(intent)
    }
}
