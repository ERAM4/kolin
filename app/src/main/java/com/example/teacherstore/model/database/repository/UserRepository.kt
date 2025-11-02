package com.example.teacherstore.model.database.repository

import com.example.teacherstore.model.database.User
import com.example.teacherstore.model.database.UserDAO
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDAO){

    val allUsers: Flow<List<User>> = userDao.findAllUsers()

    suspend fun insertUser(user: User){
        userDao.insertClient(user)
    }

    suspend fun deleteUser(user: User){
        userDao.deleteClient(user)
    }

}