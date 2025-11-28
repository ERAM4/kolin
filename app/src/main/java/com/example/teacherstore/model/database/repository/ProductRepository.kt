package com.example.teacherstore.model.database.repository

import com.example.teacherstore.model.database.DataProduct
import com.example.teacherstore.model.database.ProductDao
// Se eliminó el import de UserDAO porque no se usa y daba error
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao){

    val allProducts: Flow<List<DataProduct>> = productDao.findAllProducts()

    suspend fun insertProduct(product: DataProduct){
        productDao.insertProduct(product)
    }

    suspend fun deleteProductPerId(id: Int){
        productDao.deleteProduct(id)
    }
}