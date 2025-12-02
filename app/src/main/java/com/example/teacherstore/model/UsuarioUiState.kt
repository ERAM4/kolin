package com.example.teacherstore.model

// MODELO DE ESTADO PARA EL LOGIN
data class LoginUIState(
    val correo: String = "",   // CAMBIO: Antes era name, ahora es correo
    val password: String = ""
)

// MODELO DE ESTADO PARA EL REGISTRO (Usuario)
data class UsuarioUiState(
    // Coincide con 'username' del backend
    val username: String = "",

    val correo: String = "",
    val contrasena: String = "",
    val repetirContrasena: String = "",
    val codigopostal:String="",

    // Eliminados: direccion y pais (Tu backend no los tiene)

    val aceptaTerminos: Boolean = false,
    val errores: UsuarioErrores = UsuarioErrores()
)

// MODELO DE ERRORES DE VALIDACIÓN
data class UsuarioErrores(
    val username: String? = null,
    val correo: String? = null,
    val contrasena: String? = null,
    val repetirContrasena: String? = null,
    val codigopostal: String?=null

    // Eliminados: direccion y pais
)