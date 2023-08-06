package com.romeat.smashup.network

import com.romeat.smashup.data.dto.*
import com.romeat.smashup.network.util.ApiWrap
import retrofit2.Response
import retrofit2.http.*

interface MainService {

    /* Current user info */

    @GET("user/get")
    suspend fun getCurrentUser(@Query("id") userId: Int
    ): Response<OwnProfile>

    @GET("user/get_settings")
    suspend fun getUserSettingsBits(
    ): Response<ApiWrap<SmashupSettings>>

    @POST("user/change_setting")
    suspend fun changeUserSettingsBit(
        @Query("bit") bit: Int,
        @Query("value") value: Int,
    ): Response<ApiWrap<SmashupSettings>>

    @POST("firebase/update_token")
    suspend fun updateFcmToken(@Query("token") token: String
    ): Response<ApiWrap<Unit>>

    @POST("firebase/delete_token")
    suspend fun deleteFcmToken(@Query("token") token: String
    ): Response<ApiWrap<Unit>>


    /* Likes & Streams */

    @GET("mashup/get_all_likes")
    suspend fun getMyLikes(): Response<ApiWrap<List<Int>>>

    @POST("mashup/remove_like")
    suspend fun removeLikeFromMashup(@Query("id") id: Int
    ): Response<ApiWrap<Unit>>

    @POST("mashup/add_like")
    suspend fun addLikeToMashup(@Query("id") id: Int
    ): Response<ApiWrap<Unit>>

    @POST("mashup/add_stream")
    suspend fun addStreamToMashup(@Query("id") id: Int
    ): Response<ApiWrap<Unit>>


    /* Users */

    @GET("user/get_many")
    suspend fun getUserProfileList(@Query("id") commaSeparatedIds: String
    ): Response<ApiWrap<List<UserProfile>>>


    /* Mashups & Sources */

    @GET("mashup/get")
    suspend fun getMashupsList(@Query("id") commaSeparatedIds: String
    ): Response<ApiWrap<List<Mashup>>>

    @GET("track/get")
    suspend fun getSourcesList(@Query("id") commaSeparatedIds: String
    ): Response<ApiWrap<List<Source>>>

    @GET("track/get_popular_mashups")
    suspend fun getMashupsWithSource(@Query("id") id: Int
    ): Response<ApiWrap<List<Mashup>>>

    @GET("user/get")
    suspend fun getUserProfile(@Query("id") id: Int
    ): Response<ApiWrap<UserProfile>>


    /* Playlists */

    /*
    @GET("static/compilations")
    suspend fun getCompilationsIds(): Response<List<Int>>

     */

    @GET("recommendations/v1")
    suspend fun getRecommendationsMashupIds(
    ): Response<ApiWrap<List<Int>>>

    @GET("playlist/get")
    suspend fun getPlaylists(@Query("id") commaSeparatedIds: String
    ): Response<ApiWrap<List<Playlist>>>


    /* Search */

    @GET("mashup/search")
    suspend fun getMashupsWithName(@Query("query") query: String
    ): Response<ApiWrap<List<Mashup>>>

    @GET("playlist/search")
    suspend fun getPlaylistsWithName(@Query("query") query: String
    ): Response<ApiWrap<List<Playlist>>>

    @GET("track/search")
    suspend fun getSourcesWithName(@Query("query") query: String
    ): Response<ApiWrap<List<Source>>>

    @GET("user/search")
    suspend fun getUsersWithName(@Query("query") query: String
    ): Response<ApiWrap<List<UserProfile>>>

}