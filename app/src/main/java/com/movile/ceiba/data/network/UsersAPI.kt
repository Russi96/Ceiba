package com.movile.ceiba.data.network

import com.movile.ceiba.models.Posts
import com.movile.ceiba.models.Users
import com.movile.ceiba.util.Constants.Companion.GET_POSTS
import com.movile.ceiba.util.Constants.Companion.GET_USERS
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface UsersAPI {

    @GET(GET_USERS)
    suspend fun getAllUsers() : Response<Users>

    @GET(GET_POSTS)
    suspend fun getAllPost(@Query("userId") userId: Int) : Response<Posts>

}