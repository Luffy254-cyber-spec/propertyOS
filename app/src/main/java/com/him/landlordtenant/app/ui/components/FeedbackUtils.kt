package com.him.landlordtenant.app.ui.components

import android.content.Context
import android.widget.Toast

object FeedbackUtils {
    
    fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }
    
    fun showSuccess(context: Context, message: String) {
        showToast(context, "Success: $message")
    }
    
    fun showError(context: Context, message: String) {
        showToast(context, "Error: $message")
    }
}
