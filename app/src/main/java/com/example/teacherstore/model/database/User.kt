package com.example.teacherstore.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// AGREGAMOS ESTA LÍNEA: Le dice a Android que esto es una tabla local llamada "users"
@Entity(tableName = "users")
data class User(

    // AGREGAMOS ESTA LÍNEA: Le dice a Android que este es el ID principal
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    // Estos son los campos que coinciden con tu Spring Boot
    val username: String,
    val correo: String,
    val password: String,
    val rol: String = "USER"
)