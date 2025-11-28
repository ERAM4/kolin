package com.example.teacherstore.model.database

// LoginRequest (Sin cambios, tu backend usa 'correo')
data class LoginRequest(
    val correo: String,
    val password: String
)

// LoginResponse (Sin cambios)
data class LoginResponse(
    val token: String
)

// Usuario (ACTUALIZADO para coincidir con tu Backend)
data class Usuario(
    val id: Long? = null,

    // Coincide con 'private String username' de Java
    val username: String,

    val correo: String,

    val password: String,

    // Coincide con 'private String rol'.
    // Le pongo valor por defecto "USUARIO" para que no crashee si no lo envías desde la UI.
    val rol: String = "USER"
)