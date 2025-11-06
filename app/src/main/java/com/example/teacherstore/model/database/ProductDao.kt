package com.example.teacherstore.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.teacherstore.model.product.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM product")
    fun findAllProducts(): Flow<List<DataProduct>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: DataProduct)

    @Update
    suspend fun updateProduct(product: DataProduct)

    @Query("DELETE FROM product WHERE id = :id")
    suspend fun deleteProduct(id:Int)

}