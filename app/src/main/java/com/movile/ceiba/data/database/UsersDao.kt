package com.movile.ceiba.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.movile.ceiba.data.database.entities.PostsEntity
import com.movile.ceiba.data.database.entities.UsersEntity
import com.movile.ceiba.models.UsersItem
import kotlinx.coroutines.flow.Flow


@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(usersEntity: UsersEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(postsEntity: PostsEntity)

    @Query("SELECT * FROM users_table ORDER BY id ASC")
    fun readUsers(): Flow<List<UsersEntity>>

    @Query("SELECT * FROM posts_table WHERE userId = :userId ORDER BY id ASC")
    fun readPosts(userId: Int) : Flow<List<PostsEntity>>


    @Query("SELECT * FROM users_table WHERE name LIKE :searchQuery")
    fun searchDatabase(searchQuery: String) : Flow<List<UsersItem>>



}