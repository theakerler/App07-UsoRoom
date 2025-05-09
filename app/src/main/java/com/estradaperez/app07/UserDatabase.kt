package com.estradaperez.app07

import androidx.room.Database
import androidx.room.RoomDatabase
import com.estradaperez.app07.model.dao.UserDao
import com.estradaperez.app07.model.entity.User

@Database(entities = [User::class], version = 1)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}

