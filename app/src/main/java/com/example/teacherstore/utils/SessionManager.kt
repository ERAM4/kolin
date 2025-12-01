package com.example.teacherstore.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
    }

    // Guardar Token
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    // Obtener Token
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    // Cerrar Sesión
    fun clearData() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}