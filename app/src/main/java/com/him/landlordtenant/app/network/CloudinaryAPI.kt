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
    @POST("v1_1/yauqylbp/image/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("upload_preset") uploadPreset: RequestBody,
        @Part("public_id") publicId: RequestBody? = null
    ): Response<CloudinaryResponseDto>
}
