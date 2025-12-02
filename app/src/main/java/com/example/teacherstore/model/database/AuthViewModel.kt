package com.example.teacherstore.model.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherstore.services.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class AuthViewModel : ViewModel() {

    // --- ESTADOS (Lo que observa la pantalla) ---

    // 1. Carga (Spinner)
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // 2. Errores (Toasts rojos)
    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError = _mensajeError.asStateFlow()

    // 3. Éxito Login (Navegación al Home)
    private val _loginExitoso = MutableStateFlow<String?>(null) // Guarda el Token
    val loginExitoso = _loginExitoso.asStateFlow()

    // 4. Éxito Registro (Navegación al Login) -> ¡NUEVO!
    private val _registroExitoso = MutableStateFlow(false)
    val registroExitoso = _registroExitoso.asStateFlow()

    // --- LÓGICA DE LOGIN ---
    fun login(correo: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _mensajeError.value = null // Limpiamos errores anteriores

            try {
                val response = RetrofitClient.api.login(LoginRequest(correo, pass))

                if (response.isSuccessful && response.body() != null) {
                    // ¡ÉXITO! Guardamos el token
                    _loginExitoso.value = response.body()!!.token
                } else {
                    // ERROR DEL SERVIDOR (401, 400, etc.)
                    // Intentamos leer el mensaje que mandó el backend
                    val errorReal = response.errorBody()?.string() ?: "Credenciales incorrectas"
                    _mensajeError.value = errorReal
                }
            } catch (e: IOException) {
                // ERROR DE INTERNET (No hay wifi, servidor apagado)
                _mensajeError.value = "No se pudo conectar con el servidor. Revisa tu IP."
            } catch (e: Exception) {
                _mensajeError.value = "Error desconocido: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // --- LÓGICA DE REGISTRO ---
    fun registrar(nombre: String,codigopostal:String, correo: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _mensajeError.value = null
            _registroExitoso.value = false

            try {
                // IMPORTANTE: El rol debe coincidir con lo que espera Spring Security
                // Usualmente es "USER" o "ROLE_USER", no "USUARIO"
                val newUser = User(
                    username = nombre,
                    correo = correo,
                    password = pass,
                    rol = "USER", // <--- CAMBIO CLAVE
                    codigopostal= codigopostal
                )

                val response = RetrofitClient.api.registrar(newUser)

                if (response.isSuccessful) {
                    // ¡ÉXITO! Avisamos a la pantalla que navegue
                    _registroExitoso.value = true
                } else {
                    // ERROR (Ej: El correo ya existe)
                    val errorMsg = response.errorBody()?.string() ?: "Error al registrar (${response.code()})"
                    _mensajeError.value = errorMsg
                }
            } catch (e: IOException) {
                _mensajeError.value = "Error de conexión. Verifica tu internet."
            } catch (e: Exception) {
                _mensajeError.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Función para reiniciar estados cuando cambias de pantalla
    fun limpiarEstados() {
        _mensajeError.value = null
        _registroExitoso.value = false
        _loginExitoso.value = null
    }
}