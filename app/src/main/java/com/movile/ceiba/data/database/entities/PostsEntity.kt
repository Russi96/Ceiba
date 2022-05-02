package com.movile.ceiba.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.movile.ceiba.util.Constants.Companion.POSTS_TABLE


@Entity(tableName = POSTS_TABLE)
class PostsEntity(
    @PrimaryKey
    var id : Int,
    var userId : Int,
    var title : String,
    var body: String
)