package com.him.landlordtenant.app.data.remote

import android.content.Context
import android.net.Uri
import android.util.Log
import com.him.landlordtenant.app.network.CloudinaryAPI
import com.google.firebase.functions.FirebaseFunctions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class CloudinaryDataSource @Inject constructor(
    private val cloudinaryApi: CloudinaryAPI,
    private val firebaseFunctions: FirebaseFunctions,
    @ApplicationContext private val context: Context
) {
    
    suspend fun uploadImage(uriString: String, requestId: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            Log.d("CloudinaryDS", "Starting upload for: $uriString")
            val uri = Uri.parse(uriString)
            val fileToUpload = if (uri.scheme == "content") {
                val inputStream = context.contentResolver.openInputStream(uri) ?: throw Exception("Could not open input stream")
                val tempFile = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
                FileOutputStream(tempFile).use { outputStream ->
                    inputStream.use { it.copyTo(outputStream) }
                }
                tempFile
            } else {
                val file = File(uriString)
                if (!file.exists()) {
                    throw Exception("File does not exist at path: $uriString")
                }
                file
            }

            val requestFile = fileToUpload.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", fileToUpload.name, requestFile)
            
            // TEMPORARY: Using Unsigned Upload to allow testing without backend deployment
            // In production, signed uploads should be used for security.
            val uploadPreset = "propertyos_unsigned".toRequestBody("text/plain".toMediaTypeOrNull())
            val publicId = requestId.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = cloudinaryApi.uploadImageUnsigned(
                cloudName = "yauqylbp",
                file = body,
                uploadPreset = uploadPreset,
                publicId = publicId
            )
            
            if (response.isSuccessful && response.body() != null) {
                val url = response.body()!!.secure_url
                Log.d("CloudinaryDS", "Upload success: $url")
                Result.success(url)
            } else {
                val errorMsg = response.errorBody()?.string() ?: response.message()
                Log.e("CloudinaryDS", "Upload failed: $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e("CloudinaryDS", "Exception during upload", e)
            if (e.message?.contains("NOT_FOUND") == true || e.message?.contains("not found") == true) {
                Result.failure(Exception("Secure backend function 'generateCloudinarySignature' not found. Please ensure you have run 'firebase deploy --only functions' in your terminal."))
            } else {
                Result.failure(e)
            }
        }
    }
}
