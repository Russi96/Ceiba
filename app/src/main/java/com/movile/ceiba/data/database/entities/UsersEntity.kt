package com.movile.ceiba.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.movile.ceiba.util.Constants.Companion.USERS_TABLE



@Entity(tableName = USERS_TABLE)
class UsersEntity(
    @PrimaryKey
    var id: Int,
    var name: String,
    var phone: String,
    var email: String,
)