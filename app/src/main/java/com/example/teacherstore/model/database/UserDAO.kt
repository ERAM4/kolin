package com.example.teacherstore.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao { // Nota: He estandarizado el nombre a 'UserDao'

    // 1. INSERTAR (Usada por el ViewModel)
    // Antes se llamaba insertClient, la cambiamos a insertUser para que coincida
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    // 2. BUSCAR POR CORREO (LA QUE FALTABA)
    // Esta es vital para arreglar el error rojo "Unresolved reference: getUserByCorreo"
    @Query("SELECT * FROM users WHERE correo = :correo LIMIT 1")
    suspend fun getUserByCorreo(correo: String): User?

    // 3. LISTAR TODOS (Ya la tenías, la dejamos por si acaso)
    @Query("SELECT * FROM users")
    fun findAllUsers(): Flow<List<User>>

    // 4. ELIMINAR
    @Delete
    suspend fun deleteUser(user: User)
}