package com.movile.ceiba.data

import com.movile.ceiba.data.network.UsersAPI
import com.movile.ceiba.models.Posts
import com.movile.ceiba.models.Users
import retrofit2.Response
import javax.inject.Inject


class RemoteDataSource @Inject constructor(private val usersAPI: UsersAPI) {

    suspend fun getAllUsers(): Response<Users> {
        return usersAPI.getAllUsers()
    }

    suspend fun getAllPost(userId: Int): Response<Posts> {
        return usersAPI.getAllPost(userId)
    }
}