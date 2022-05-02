package com.movile.ceiba.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.movile.ceiba.data.database.entities.PostsEntity
import com.movile.ceiba.data.database.entities.UsersEntity


@Database(entities = [UsersEntity::class, PostsEntity::class], version = 1, exportSchema = false)
@TypeConverters(UsersTypeConvert::class)
abstract class UsersDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
}