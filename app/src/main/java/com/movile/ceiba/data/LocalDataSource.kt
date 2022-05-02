package com.movile.ceiba.data

import com.movile.ceiba.data.database.UsersDao
import com.movile.ceiba.data.database.entities.PostsEntity
import com.movile.ceiba.data.database.entities.UsersEntity
import com.movile.ceiba.models.UsersItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val usersDao: UsersDao
) {

    fun readUsers(): Flow<List<UsersEntity>> {
        return usersDao.readUsers()
    }

    fun readPosts(userId: Int): Flow<List<PostsEntity>>{
        return usersDao.readPosts(userId)
    }

    fun searchUsers(searchQuery: String): Flow<List<UsersItem>> {
        return usersDao.searchDatabase(searchQuery)
    }

    suspend fun insertUsers(usersEntity: UsersEntity) {
        usersDao.insertUsers(usersEntity)
    }

    suspend fun insertPosts(postsEntity: PostsEntity){
        usersDao.insertPosts(postsEntity)
    }


}