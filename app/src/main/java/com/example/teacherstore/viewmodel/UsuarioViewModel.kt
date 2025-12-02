package com.example.teacherstore.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.teacherstore.model.LoginUIState
import com.example.teacherstore.model.UsuarioErrores
import com.example.teacherstore.model.UsuarioUiState
import com.example.teacherstore.model.database.User
import com.example.teacherstore.model.database.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsuarioViewModel(private val userRepository: UserRepository) : ViewModel() {

    // ESTADO INTERNO
    private val _estado = MutableStateFlow(UsuarioUiState())
    val estado: StateFlow<UsuarioUiState> = _estado.asStateFlow()

    private val _loginState = MutableStateFlow(LoginUIState())
    val loginState: StateFlow<LoginUIState> = _loginState.asStateFlow()

    // --- FUNCIONES (Inputs) ---
    fun onNombreChange(nuevoNombre: String) {
        _estado.update { it.copy(username = nuevoNombre, errores = it.errores.copy(username = null)) }
    }

    fun onCorreoChange(nuevoCorreo: String) {
        _estado.update { it.copy(correo = nuevoCorreo, errores = it.errores.copy(correo = null)) }
    }

    fun onContrasenaChange(nuevaContrasena: String) {
        _estado.update { it.copy(contrasena = nuevaContrasena, errores = it.errores.copy(contrasena = null)) }
    }

    fun onRepetirContrasenaChange(repetir: String) {
        _estado.update { it.copy(repetirContrasena = repetir, errores = it.errores.copy(repetirContrasena = null)) }
    }
    fun onCodigoChange(nuevoCodigopostal: String) {
        _estado.update { it.copy(codigopostal = nuevoCodigopostal, errores = it.errores.copy(codigopostal = null)) }
    }
    fun onAceptarTerminosChange(acepta: Boolean) {
        _estado.update { it.copy(aceptaTerminos = acepta) }
    }

    // --- LOGIN ---
    fun onNameChangeLogin(nuevoCorreo: String) {
        _loginState.update { it.copy(correo = nuevoCorreo) }
    }

    fun onPasswordChangeLogin(nuevaPass: String) {
        _loginState.update { it.copy(password = nuevaPass) }
    }

    // --- VALIDACIÓN ---
    fun estaValidadoElFormulario(): Boolean {
        val formularioActual = _estado.value
        val errores = UsuarioErrores(
            username = if (formularioActual.username.isBlank()) "El campo es obligatorio" else null,
            correo = if (!Patterns.EMAIL_ADDRESS.matcher(formularioActual.correo).matches()) "Correo inválido" else null,
            contrasena = if (formularioActual.contrasena.length < 4) "Mínimo 4 caracteres" else null,
            repetirContrasena = if (formularioActual.contrasena != formularioActual.repetirContrasena) "No coinciden" else null,
            codigopostal = if (formularioActual.codigopostal.isBlank()) "El campo es obligatorio" else null,
        )

        val hayErrores = listOfNotNull(errores.username, errores.correo, errores.contrasena, errores.repetirContrasena,errores.codigopostal).isNotEmpty()
        _estado.update { it.copy(errores = errores) }
        return !hayErrores
    }

    // --- BASE DE DATOS LOCAL ---
    fun insertUser(user: User) = viewModelScope.launch {
        userRepository.insertUser(user)
    }

    suspend fun userExist(email: String, password: String): Boolean {
        val user = userRepository.getUserByCorreo(email)
        return user != null && user.password == password
    }

    // --- ¡AQUÍ ESTÁ LA FACTORY QUE TE FALTABA! ---
    // Debe estar DENTRO de la clase UsuarioViewModel (antes de la última llave de cierre)
    class UsuarioViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UsuarioViewModel(userRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}