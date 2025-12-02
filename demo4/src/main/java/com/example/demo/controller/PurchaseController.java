package com.example.demo.controller;

import com.example.demo.model.Purchase;

import com.example.demo.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
@Tag(name = "Gestion de Compras")
public class PurchaseController {
    @Autowired PurchaseService purchaseService;

    @GetMapping
    @Operation(summary =  "Obtener todas las compras")
    public List<Purchase> findAllPurchase() {
        return purchaseService.findAllPurchase();
    }

    @GetMapping("/{id}")
    @Operation(summary =  "Obtener una compra por id")
    public Purchase  getPurchaseById(@PathVariable Long id) {
        return purchaseService.getPurchaseById(id);

    }

    @GetMapping("/user/{user_id}")
    @Operation(summary =  "Obtener una compra por el id del usuario")
    public Purchase  findUserPurchaseById(@PathVariable Long user_id) {
        return purchaseService.findUserPurchaseById(user_id);

    }

    @PostMapping
    @Operation(summary =  "Crear la compra")
    public Purchase savePurchase (@RequestBody Purchase purchase) {
        return purchaseService.savePurchase(purchase);
    }
}
