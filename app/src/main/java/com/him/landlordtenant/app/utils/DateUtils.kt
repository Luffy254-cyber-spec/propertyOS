package com.him.landlordtenant.app.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    private const val DEFAULT_FORMAT = "dd MMM yyyy"
    private const val TIME_FORMAT = "hh:mm a"
    private const val FULL_FORMAT = "dd MMM yyyy, hh:mm a"

    fun format(timestamp: Long, format: String = DEFAULT_FORMAT): String {
        return try {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            sdf.format(Date(timestamp))
        } catch (e: Exception) {
            ""
        }
    }

    fun formatTime(timestamp: Long): String = format(timestamp, TIME_FORMAT)
    
    fun formatFull(timestamp: Long): String = format(timestamp, FULL_FORMAT)

    fun getDaysDifference(timestamp: Long): Long {
        val diff = timestamp - System.currentTimeMillis()
        return diff / (1000 * 60 * 60 * 24)
    }
}
