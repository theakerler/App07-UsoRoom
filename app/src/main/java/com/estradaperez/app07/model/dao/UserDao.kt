package com.estradaperez.app07.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.estradaperez.app07.model.entity.User

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    suspend fun getAll(): List<User>

    @Insert
    suspend fun insert(user: User)
}


