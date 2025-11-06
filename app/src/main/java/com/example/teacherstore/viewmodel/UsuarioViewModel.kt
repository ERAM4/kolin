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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first

import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsuarioViewModel(private val userRepository: UserRepository) : ViewModel() {

    val allClients = userRepository.allUsers

    private val _loginState = MutableStateFlow(LoginUIState())
    val loginState: StateFlow<LoginUIState> = _loginState


    //declaramos el estado interno mutable
    private val _estado= MutableStateFlow(UsuarioUiState())
    //expone el estado de manera publica y es de solo lectura
    val estado: StateFlow<UsuarioUiState> = _estado

    //actualiza el campo nombre
    fun onNombreChange(nuevoNombre:String){
        _estado.update { it.copy(nombre = nuevoNombre, errores = it.errores.copy(nombre=null)) }
    }
    //actualiza el campo correo
    fun onCorreoChange(nuevoCorreo:String){
        _estado.update { it.copy(correo = nuevoCorreo, errores = it.errores.copy(correo =null)) }
    }

    fun onContrasenaChange(nuevaContrasena:String){
        _estado.update { it.copy(contrasena = nuevaContrasena, errores = it.errores.copy(contrasena =null)) }
    }
    fun onRepetirContrasenaChange(repetirContrasena:String){
        _estado.update { it.copy(repetirContrasena = repetirContrasena, errores = it.errores.copy(repetirContrasena =null)) }
    }

    fun onDireccionChange(nuevaDireccion:String){
        _estado.update { it.copy(direccion = nuevaDireccion, errores = it.errores.copy(direccion =null)) }
    }
    fun onPaisChange(pais:String){
        _estado.update { it.copy(pais = pais, errores = it.errores.copy(pais =null)) }
    }

    fun onAceptarTerminosChange(nuevoAceptarTerminos: Boolean){
        _estado.update { it.copy(aceptaTerminos = nuevoAceptarTerminos) }
    }

    fun onNameChangeLogin(newValue:String){
        _loginState.update { it.copy(name = newValue) }
    }

    fun onPasswordChangeLogin(newValue:String){
        _loginState.update { it.copy(password = newValue) }
    }



    //validacion global del formulario

    fun estaValidadoElFormulario(): Boolean{
        //el estado actual del formulario
        val formularioActual=_estado.value
        val errores= UsuarioErrores(
            nombre = if(formularioActual.nombre.isBlank()) "El campo es obligatorio" else null,
            correo = if(!Patterns.EMAIL_ADDRESS.matcher(formularioActual.correo).matches()) "El correo debe ser valido" else null,
            repetirContrasena = if(formularioActual.contrasena == formularioActual.repetirContrasena)  null else "Las contraseñas no coinciden",
            contrasena= if(formularioActual.contrasena.length <6)"La contraseña debe tener al menos 6 caracteres" else null,
            direccion = if(formularioActual.direccion.isBlank()) "El campo es obligatorio" else null,
            pais = if(formularioActual.pais.isBlank()) "El campo es obligatorio" else null,
        )

        //listOfNotNull retorna una lista de los elementos que "no sean nulos"
        val hayErrores=listOfNotNull(
            errores.nombre,
            errores.correo,
            errores.contrasena,
            errores.repetirContrasena,
            errores.pais,
            errores.direccion
        ).isNotEmpty()//retorna true si la coleccion no esta vacia

        _estado.update { it.copy(errores=errores) }

        return !hayErrores

        //return !hayErrores


    }

    class UsuarioViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory{

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return UsuarioViewModel(userRepository) as T
            }
            throw IllegalArgumentException("Ni modo flaco no esta la vista del modelo")
        }
    }

    fun insertUser(user: User) = viewModelScope.launch{
        userRepository.insertUser(user)
    }

    fun deleteClient(user: User) = viewModelScope.launch {
        userRepository.deleteUser(user) // Asumiendo que tienes una función deleteUser en tu repositorio
    }

    suspend fun userExist(email: String, password: String): Boolean {
        val users = allClients.first()
        return users.any { user ->
            user.email == email && user.password == password
        }
    }

}