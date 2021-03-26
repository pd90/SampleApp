package com.sample.data.local

import androidx.room.Dao
import androidx.room.Insert
import com.sample.data.model.User

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User)
}