package com.sample.data.local

/**
 * Base abstract class for room Database
 */
import androidx.room.Database
import androidx.room.RoomDatabase
import com.sample.data.model.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}