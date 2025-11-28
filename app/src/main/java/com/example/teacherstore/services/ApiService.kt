package com.example.teacherstore.services

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
// IMPORTANTE: Asegúrate de importar la clase User correcta
import com.example.teacherstore.model.database.User
import com.example.teacherstore.model.database.LoginRequest
import com.example.teacherstore.model.database.LoginResponse

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // CAMBIO: Aquí dice 'user: User' en vez de 'usuario: Usuario'
    @POST("api/usuarios")
    suspend fun registrar(@Body user: User): Response<User>
}