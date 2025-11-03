

package com.example.teacherstore.model.product

// Clase de datos para representar un producto
data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String
    // Puedes añadir más campos como imageUrl, stock, etc.
)


