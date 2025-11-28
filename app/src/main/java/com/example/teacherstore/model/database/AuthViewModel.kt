package com.example.teacherstore.model.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherstore.model.database.LoginRequest
import com.example.teacherstore.model.database.User
import com.example.teacherstore.services.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    // ESTADOS
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError = _mensajeError.asStateFlow()

    private val _loginExitoso = MutableStateFlow<String?>(null)
    val loginExitoso = _loginExitoso.asStateFlow()

    // --- FUNCIÓN LOGIN (La que te estaba fallando) ---
    fun login(correo: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _mensajeError.value = null

            try {
                // Llamada al servidor
                val response = RetrofitClient.api.login(LoginRequest(correo, pass))

                if (response.isSuccessful && response.body() != null) {
                    val token = response.body()!!.token
                    _loginExitoso.value = token
                } else {
                    _mensajeError.value = "Credenciales incorrectas"
                }
            } catch (e: Exception) {
                _mensajeError.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // --- FUNCIÓN REGISTRO ---
    fun registrar(nombre: String, correo: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _mensajeError.value = null

            try {
                // Creamos el usuario (asegurando el rol USUARIO)
                val newUser = User(
                    username = nombre,
                    correo = correo,
                    password = pass,
                    rol = "USUARIO"
                )

                val response = RetrofitClient.api.registrar(newUser)

                if (response.isSuccessful) {
                    _mensajeError.value = "¡Registro exitoso! Inicia sesión."
                } else {
                    _mensajeError.value = "Error al registrar: ${response.code()}"
                }
            } catch (e: Exception) {
                _mensajeError.value = "Error de red: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}