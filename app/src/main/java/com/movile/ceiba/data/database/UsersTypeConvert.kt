package com.movile.ceiba.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.movile.ceiba.models.Users


class UsersTypeConvert {

    var gson = Gson()

    @TypeConverter
    fun usersToString(users: Users) : String{
        return gson.toJson(users)
    }

    @TypeConverter
    fun stringToUsers(data: String) : Users{
        val listType = object : TypeToken<Users>() {}.type
        return gson.fromJson(data, listType)
    }
}