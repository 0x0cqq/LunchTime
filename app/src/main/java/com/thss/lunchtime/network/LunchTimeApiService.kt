package com.thss.lunchtime.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
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
    "http://lunchtime.cqqqwq.com:8000"


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

    @FormUrlEncoded
    @POST("/api/register")
    suspend fun register(@Field("name") name: String, @Field("password") password: String,
                         @Field("email") email: String, @Field("verification") code: String): Response

    @GET("/api/post_detail")
    suspend fun getPostDetail(@Query("user_name") name : String, @Query("post_id") postID: Int) : ResponseWithPostDetail

    @GET("/api/posts")
    suspend fun getPostList(@Query("user_name") name : String, @Query("type") type: Int, @Query("target_user_name") targetName: String = "", @Query("filter")filter: Int) : ResponseWithPostList

    @GET("/api/posts_saved")
    suspend fun getPostListSaved(@Query("user_name") name : String,  @Query("target_user_name") targetName: String = "") : ResponseWithPostList

    @GET("/api/search_post")
    suspend fun getPostListSearched(@Query("user_name") name: String, @Query("field") field: String, @Query("keyword") keyword: String): ResponseWithPostList

    // see https://stackoverflow.com/questions/39866676/retrofit-uploading-multiple-images-to-a-single-key
    @Multipart
    @POST("/api/post")
    suspend fun post(@Part("user_name") userName: RequestBody,
                     @Part("title") title: RequestBody, @Part("content") content: RequestBody,
                     @Part("location") location: RequestBody, @Part("tag") tag: RequestBody,
                     @Part images: List<MultipartBody.Part>, @Part videos: List<MultipartBody.Part>): ResponseWithPostID

    @FormUrlEncoded
    @POST("/api/love_post")
    suspend fun likePost(@Field("user_name") name: String, @Field("post_id") postID: Int): ResponseWithResult

    @FormUrlEncoded
    @POST("/api/save_post")
    suspend fun starPost(@Field("user_name") name: String, @Field("post_id") postID: Int): ResponseWithResult

    @FormUrlEncoded
    @POST("/api/comment_post")
    suspend fun commentPost(@Field("user_name") name: String, @Field("post_id")postID: Int, @Field("comment") comment : String) : Response

    @GET("/api/notice")
    suspend fun getNotice(@Query("user_name") name: String, @Query("type") type: Int): ResponseWithNotice

    @FormUrlEncoded
    @POST("api/read_notice")
    suspend fun readNotice(@Field("user_name") name: String, @Field("target_user_name") targetName: String, @Field("type") type: String, @Field("create_time") createTime: Long, @Field("post_id") postID: Int): Response

    @GET("/api/user_info")
    suspend fun getUserInfo(@Query("user_name") name: String, @Query("target_user_name") target_name: String): ResponseWithUserInfo

    @FormUrlEncoded
    @POST("/api/modify_user_name")
    suspend fun modifyUserName(@Field("original_user_name") old_name: String, @Field("new_user_name") new_name: String): Response

    @FormUrlEncoded
    @POST("/api/modify_user_description")
    suspend fun modifyUserDescription(@Field("user_name") name: String, @Field("new_description") description: String): Response

    @FormUrlEncoded
    @POST("/api/modify_user_password")
    suspend fun modifyUserPassword(@Field("user_name") name: String, @Field("old_password") old_password: String, @Field("new_password") new_password: String): Response

    @Multipart
    @POST("api/modify_user_image")
    suspend fun modifyUserImage(@Part("user_name") name: RequestBody, @Part image: List<MultipartBody.Part>): Response

    @FormUrlEncoded
    @POST("/api/attention")
    suspend fun FollowUser(@Field("user_name") name: String, @Field("target_user_name") targetName: String): ResponseWithResult

    @FormUrlEncoded
    @POST("/api/hate")
    suspend fun BlockUser(@Field("user_name") name: String, @Field("target_user_name") targetName: String): ResponseWithResult

    @GET("/api/attention_list")
    suspend fun getAttentionList(@Query("user_name") name: String, @Query("type") type: Int): ResponseWithUserList

    @GET("/api/chats")
    suspend fun getChatList(@Query("user_name") name: String): ResponseWithChatList
    @FormUrlEncoded
    @POST("api/chats")
    suspend fun readChat(@Field("user_name") name: String, @Field("target_user_name") targetName: String): Response
}

object LunchTimeApi {
    val retrofitService: LunchTimeApiService by lazy {
        retrofit.create(LunchTimeApiService::class.java)
    }
}
