package com.him.landlordtenant.app.network

import com.him.landlordtenant.app.network.dto.CloudinaryResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CloudinaryAPI {
    @Multipart
    @POST("v1_1/{cloud_name}/image/upload")
    suspend fun uploadImageSigned(
        @retrofit2.http.Path("cloud_name") cloudName: String,
        @Part file: MultipartBody.Part,
        @Part("api_key") apiKey: RequestBody,
        @Part("timestamp") timestamp: RequestBody,
        @Part("signature") signature: RequestBody,
        @Part("public_id") publicId: RequestBody? = null
    ): Response<CloudinaryResponseDto>

    @Multipart
    @POST("v1_1/{cloud_name}/image/upload")
    suspend fun uploadImageUnsigned(
        @retrofit2.http.Path("cloud_name") cloudName: String,
        @Part file: MultipartBody.Part,
        @Part("upload_preset") uploadPreset: RequestBody,
        @Part("public_id") publicId: RequestBody? = null
    ): Response<CloudinaryResponseDto>
}
