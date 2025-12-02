package com.example.demo.controller;

import com.example.demo.model.Product;

import com.example.demo.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Gestion de Productos")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping
    @Operation(summary =  "Obtener todos los Producto ")
    public List<Product> findAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping("/{id}")
    @Operation(summary =  "Obtener un Producto por el id ")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);

    }

    @PostMapping
    @Operation(summary =  "Guardar un Producto ")
    public Product saveProduct (@RequestBody Product product) {
        return productService.saveProduct(product);
    }


    @PutMapping("/{id}")
    @Operation(summary =  "Actualizar un Producto ")
    public Product  updateProduct  (@PathVariable Long id, @RequestBody Product  product) {

        return productService.updateProduct (id,product);
    }

    @DeleteMapping("/{id}")
    @Operation(summary =  "Eliminar un Producto ")
    public void deleteProduct (@PathVariable Long id){
        productService.deleteProduct(id);
}
}
