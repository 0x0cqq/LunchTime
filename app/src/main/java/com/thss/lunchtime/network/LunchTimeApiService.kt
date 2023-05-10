package com.thss.lunchtime.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

private const val BASE_URL =
    "http://lunchtime.cqqqwq.com:8000"


@OptIn(ExperimentalSerializationApi::class)
private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
    .baseUrl(BASE_URL)
    .build()



interface LunchTimeApiService {
    @FormUrlEncoded
    @POST("/api/verify_email")
    suspend fun getEmailVerificationCode(@Field("email") email: String): Response

    @FormUrlEncoded
    @POST("/api/login")
    suspend fun login(@Field("name") name: String, @Field("password") password: String): Response

    @FormUrlEncoded
    @POST("/api/register")
    suspend fun register(@Field("name") name: String, @Field("password") password: String,
                         @Field("email") email: String, @Field("verification") code: String): Response
}

object LunchTimeApi {
    val retrofitService: LunchTimeApiService by lazy {
        retrofit.create(LunchTimeApiService::class.java)
    }
}
