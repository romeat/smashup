package com.romeat.smashup.network

import com.romeat.smashup.data.dto.*
import retrofit2.Response
import retrofit2.http.*

interface MainService {

    /* Current user info */

    @GET("user/get")
    suspend fun getCurrentUser(@Query("username") user: String
    ): Response<OwnProfile>


    /* Mashups & Sources */

    @GET("mashup/get")
    suspend fun getMashupsList(@Query("id") commaSeparatedIds: String
    ): Response<List<Mashup>>

    @GET("track/get")
    suspend fun getSourcesList(@Query("id") commaSeparatedIds: String
    ): Response<List<Source>>

    @GET("track/get_popular_mashups")
    suspend fun getMashupsWithSource(@Query("id") id: Int
    ): Response<List<Mashup>>

    @GET("user/get")
    suspend fun getAuthorProfile(@Query("username") user: String
    ): Response<AuthorProfile>


    /* Playlists */

    @GET("static/compilations")
    suspend fun getCompilationsIds(): Response<List<Int>>

    @GET("playlist/get")
    suspend fun getPlaylists(@Query("id") commaSeparatedIds: String
    ): Response<List<Playlist>>


    /* Search */

    @GET("search/mashups")
    suspend fun getMashupsWithName(@Query("query") query: String
    ): Response<List<Mashup>>

    @GET("search/playlists")
    suspend fun getPlaylistsWithName(@Query("query") query: String
    ): Response<List<Playlist>>

    @GET("search/tracks")
    suspend fun getSourcesWithName(@Query("query") query: String
    ): Response<List<Source>>

    @GET("search/users")
    suspend fun getUsersWithName(@Query("query") query: String
    ): Response<List<AuthorProfile>>

}