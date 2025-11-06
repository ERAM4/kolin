package com.example.teacherstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teacherstore.model.database.DataProduct
import com.example.teacherstore.model.database.repository.ProductRepository
import com.example.teacherstore.model.database.repository.UserRepository
import kotlinx.coroutines.launch

class ProductViewModel(private val productRepository: ProductRepository): ViewModel() {

    val allProductsInCart = productRepository.allProducts

    fun insertProduct(dataProduct: DataProduct) = viewModelScope.launch {
        productRepository.insertProduct(dataProduct)
    }

    fun deleteProductPerId(id: Int) = viewModelScope.launch {
        productRepository.deleteProductPerId(id)
    }


    class DataProductViewModelFactory(private val productRepository: ProductRepository) : ViewModelProvider.Factory{

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return ProductViewModel(productRepository) as T
            }
            throw IllegalArgumentException("Ni modo flaco no esta la vista del modelo")
        }
    }

}