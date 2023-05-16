package com.thss.lunchtime.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

private const val BASE_URL =
    "http://82.156.30.206:8000"


private val myJson: Json = Json { ignoreUnknownKeys = true }

@OptIn(ExperimentalSerializationApi::class)
private val retrofit = Retrofit.Builder()
    .addConverterFactory(myJson.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()



interface LunchTimeApiService {
    @FormUrlEncoded
    @POST("/api/verify_email")
    suspend fun getEmailVerificationCode(@Field("email") email: String): Response

    @FormUrlEncoded
    @POST("/api/login")
    suspend fun login(@Field("name") name: String, @Field("password") password: String): Response


    @GET("/api/post_detail")
    suspend fun getPostDetail(@Query("user_name") name : String, @Query("post_id") postID: Int) : ResponseWithPostDetail

    @GET("/api/posts")
    suspend fun getPostList(@Query("user_name") name : String, @Query("type") type: Int) : ResponseWithPostList

    @FormUrlEncoded
    @POST("/api/register")
    suspend fun register(@Field("name") name: String, @Field("password") password: String,
                         @Field("email") email: String, @Field("verification") code: String): Response


    // see https://stackoverflow.com/questions/39866676/retrofit-uploading-multiple-images-to-a-single-key
    @Multipart
    @POST("/api/post")
    suspend fun post(@Part("user_name") userName: RequestBody,
                     @Part("title") title: RequestBody, @Part("content") content: RequestBody,
                     @Part("location") location: RequestBody, @Part("tag") tag: RequestBody,
                     @Part images: List<MultipartBody.Part>): ResponseWithPost

    @GET("api/notice")
    suspend fun getNotice(@Query("user_name") name: String, @Query("type") type: Int): ResponseWithNotice
}

object LunchTimeApi {
    val retrofitService: LunchTimeApiService by lazy {
        retrofit.create(LunchTimeApiService::class.java)
    }
}
