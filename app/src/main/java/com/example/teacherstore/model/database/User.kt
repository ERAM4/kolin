package com.example.teacherstore.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class User (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val email: String = "",
    val password: String = "",
){
}