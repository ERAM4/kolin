package com.example.demo.service;

import com.example.demo.model.Purchase;

import com.example.demo.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PurchaseService {
    @Autowired
    private PurchaseRepository purchaseRepository;

    public Purchase savePurchase(Purchase purchase){
        return purchaseRepository.save(purchase);}

    public List<Purchase> findAllPurchase() {
        return purchaseRepository.findAll();
    }

    public Purchase getPurchaseById(Long id) {
        return purchaseRepository.findById(id).orElse(null);
    }

    public Purchase findUserPurchaseById(Long user_id) {
        return purchaseRepository.findById(user_id).orElse(null);
    }
}
