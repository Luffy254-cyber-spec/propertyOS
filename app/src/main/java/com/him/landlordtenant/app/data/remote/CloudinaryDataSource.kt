package com.him.landlordtenant.app.data.remote

import com.cloudinary.android.MediaManager
import javax.inject.Inject

import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class CloudinaryDataSource @Inject constructor() {
    
    suspend fun uploadImage(filePath: String, requestId: String): Result<String> = suspendCancellableCoroutine { continuation ->
        MediaManager.get().upload(filePath)
            .unsigned("propertyos_unsigned") // You'll need to create an unsigned upload preset in Cloudinary
            .option("public_id", requestId)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {}
                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}
                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val url = resultData["secure_url"] as? String ?: resultData["url"] as? String
                    if (url != null) {
                        continuation.resume(Result.success(url))
                    } else {
                        continuation.resume(Result.failure(Exception("Upload successful but URL missing")))
                    }
                }
                override fun onError(requestId: String, error: ErrorInfo) {
                    continuation.resume(Result.failure(Exception(error.description)))
                }
                override fun onReschedule(requestId: String, error: ErrorInfo) {}
            })
            .dispatch()
    }
}


