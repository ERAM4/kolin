package com.example.teacherstore.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
class DataProduct(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val productName: String = "",
        val price: Double = 0.0,
        val description: String = "",
        val imageUrl: String = ""
){

}

