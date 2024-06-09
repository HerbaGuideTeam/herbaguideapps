package com.capstone.herbaguideapps.data.remote.api

import com.capstone.herbaguideapps.data.remote.LoginBody
import com.capstone.herbaguideapps.data.remote.RegisterBody
import com.capstone.herbaguideapps.data.remote.response.AuthResponse
import com.capstone.herbaguideapps.data.remote.response.FileUploadResponse
import com.capstone.herbaguideapps.data.remote.response.NewsResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @GET("everything")
    suspend fun getHerbalNews(
        @Query("apiKey") apikey: String,
        @Query("q") q: String = "herbal",
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") page: Int = 1
    ): NewsResponse

    @GET("everything")
    fun topHeadline(
        @Query("apiKey") apikey: String,
        @Query("q") q: String = "herbal+medicine"
    ): Call<NewsResponse>

    @POST("login")
    fun login(
        @Body loginBody: LoginBody
    ): Call<AuthResponse>

    @POST("signup")
    fun register(
        @Body registerBody: RegisterBody
    ): Call<AuthResponse>

    @Multipart
    @POST("skin-cancer/predict")
    fun uploadImage(
        @Part file: MultipartBody.Part
    ): Call<FileUploadResponse>
}