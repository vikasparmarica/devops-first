package com.azure.devops.first.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private List<com.azure.devops.first.model.Item> items = new ArrayList<>();

    public List<com.azure.devops.first.model.Item> findAllItems() {
        return items;
    }

    public com.azure.devops.first.model.Item findById(String id) {
        com.azure.devops.first.model.Item defaultItem = new com.azure.devops.first.model.Item("NULL", "NULL", 0, 0.0);
        return items.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElse(defaultItem);
    }

    public boolean isItemExist(com.azure.devops.first.model.Item item) {
        return items.stream()
                .anyMatch(item1 -> item1.getId().equals(item.getId()));
    }

    public void saveCart(com.azure.devops.first.model.Item item) {
        items.add(item);
    }

    public void deleteCartById(com.azure.devops.first.model.Item item) {
        items.remove(item);
    }

    public void ClearAllItems() {
        items.clear();
    }
}
