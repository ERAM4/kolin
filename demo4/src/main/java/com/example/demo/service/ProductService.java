package com.example.demo.service;
import com.example.demo.model.Product;

import com.example.demo.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;




    public Product saveProduct(Product product){
        return productRepository.save(product);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product updateProduct(Long id, Product productActualizado) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(productActualizado.getName());
                    product.setPrice(productActualizado.getPrice());
                    product.setImg(productActualizado.getImg());
                    product.setRating(productActualizado.getRating());
                    product.setDescription(productActualizado.getDescription());
                    product.setDistribuidor(productActualizado.getDistribuidor());

                    return productRepository.save(product);
                })
                .orElse(null);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
