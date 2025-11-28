package com.example.teacherstore.model.database.repository

import com.example.teacherstore.model.database.User
import com.example.teacherstore.model.database.UserDao // CORREGIDO: UserDao
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao){ // CORREGIDO: UserDao

    // Esta función la mantuvimos en el DAO, así que funciona
    val allUsers: Flow<List<User>> = userDao.findAllUsers()

    suspend fun insertUser(user: User){
        // CORREGIDO: Antes era insertClient, ahora es insertUser
        userDao.insertUser(user)
    }

    suspend fun deleteUser(user: User){
        // CORREGIDO: Antes era deleteClient, ahora es deleteUser
        userDao.deleteUser(user)
    }

    // AGREGADO: Esta función es OBLIGATORIA para que el ViewModel no de error
    suspend fun getUserByCorreo(correo: String): User? {
        return userDao.getUserByCorreo(correo)
    }
}